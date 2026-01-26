package backend.bookSharing.services.user;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.OwnedRepository;
import backend.bookSharing.repository.RegionRepository;
import backend.bookSharing.repository.TokenRepository;
import backend.bookSharing.repository.UserRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Lend;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.OwnedId;
import backend.bookSharing.repository.entities.Region;
import backend.bookSharing.repository.entities.Request;
import backend.bookSharing.repository.entities.Token;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.services.PasswordValidation;
import backend.bookSharing.services.user.services.TokenValidation;
import io.vavr.control.Either;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    private final RegionRepository regionRepo;

    private final OwnedRepository ownedRepo;

    private final BookRepository bookRepo;

    private final TokenRepository tokenRepo;

    private final PasswordValidation passwordValidation;

    private final TokenValidation tokenValidation;

    @ConfigurationProperties
    public long bookCount() {
        return userRepo.count();
    }

    @Override
    public List<Book> getOwnedBooks(Integer userId) {
        return userRepo.getReferenceById(userId).getOwned().stream().map(Owned::getBook).toList();
    }

    @Override
    public List<Request> getRequestsOfBook(Integer ownerId, Integer bookId) {
        return userRepo.getReferenceById(ownerId).getOwned()
                .stream().filter(owned -> owned.getBook().getId().equals(bookId)).toList().getFirst()
                .getRequests().stream().toList();
    }

    @Override
    public Lend getLendOfBook(Integer ownerId, Integer bookId) {
        return userRepo.getReferenceById(ownerId).getOwned()
                .stream().filter(owned -> owned.getBook().getId().equals(bookId)).toList().getFirst()
                .getLend();
    }

    @Override
    public User checkAuthentication(String token) {

        if (!tokenValidation.canBeToken(token)){
            return null;
        }

        Optional<Token> searchedToken = tokenRepo.findById(tokenValidation.createTokenValidationInformation(token));

        if (searchedToken.isEmpty()){
            return null;
        }

        Token retrievedToken = searchedToken.get();

        if (tokenValidation.hasTokenExpired(Instant.now(), retrievedToken)){
            tokenRepo.delete(retrievedToken);
            return null;
        }

        retrievedToken.setLast_used(Timestamp.from(Instant.now()));

        return tokenRepo.save(retrievedToken).getUser();

    }

    @Override
    public Either<UserCreationError, Integer> createUser(String email, String password) {

        try {
            passwordValidation.isSafePassword(password);
        } catch (Exception e) {
            return Either.left(new UserCreationError.WeakPassword()); //todo communicate more specific
        }

        Optional<User> emailSearch = userRepo.findByEmail(email);

        if (emailSearch.isPresent()) {
            return Either.left(new UserCreationError.EmailInUse());
        }

        User created = userRepo.save(new User(
                new Region("Portugal"), //todo missing
                email,
                passwordValidation.passwordEncoding(password)
        ));

        return Either.right(created.getId());
    }

    @Override
    public Either<UserAuthenticationError, String> login(String email, String password) {

        Optional<User> searchedUser = userRepo.findByEmail(email);

        if (searchedUser.isEmpty()) {
            return Either.left(new UserAuthenticationError.UserOrPasswordAreInvalid());
        }

        User user = searchedUser.get();

        if (!passwordValidation.validatePassword(password, user.getPassword())) {
            return Either.left(new UserAuthenticationError.UserOrPasswordAreInvalid());
        }

        String token = tokenValidation.generateTokenValue();

        Token createdToken = new Token(tokenValidation.createTokenValidationInformation(token), searchedUser.get());

        tokenRepo.save(createdToken);

        return Either.right(token);

    }

    @Override
    public Either<LogoutError, Void> logout(String token) {
        return Either.right(Void.TYPE.cast(this));
    }

    @Override
    public Either<OwnerShipAdditionError, Owned> addOwner(String isbn, String token) {

        User user = checkAuthentication(token);

        if (user == null){
            return Either.left(new OwnerShipAdditionError.UserAuthenticationInvalid());
        }

        boolean isIsbn10 = isbn.length() == 10;

        Book searchedBook;

        if (isIsbn10) {
            searchedBook = bookRepo.findByIsbnTen(isbn);
        } else {
            searchedBook = bookRepo.findByIsbnThirteen(isbn);
        }

        if (searchedBook == null) {
            return Either.left(new OwnerShipAdditionError.BookNotFound());
        }

        OwnedId ownedId = new OwnedId(user.getId(), searchedBook.getId());

        if (ownedRepo.findById(ownedId).isPresent()){
            return Either.left(new OwnerShipAdditionError.AlreadyMarkedAsOwned());
        }

        return Either.right(ownedRepo.save(new Owned(ownedId)));

    }


}
