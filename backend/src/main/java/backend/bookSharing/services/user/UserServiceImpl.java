package backend.bookSharing.services.user;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.OwnedRepository;
import backend.bookSharing.repository.RegionRepository;
import backend.bookSharing.repository.TokenRepository;
import backend.bookSharing.repository.UserRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Lend;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.Region;
import backend.bookSharing.repository.entities.Request;
import backend.bookSharing.repository.entities.Token;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.failures.LogoutError;
import backend.bookSharing.services.user.failures.OwnerShipAdditionError;
import backend.bookSharing.services.user.failures.UserAuthenticationError;
import backend.bookSharing.services.user.failures.UserCreationError;
import backend.bookSharing.services.user.services.PasswordValidation;
import backend.bookSharing.services.user.services.TokenValidation;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

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

        if (!tokenValidation.canBeToken(token)) {
            return null;
        }

        Optional<Token> searchedToken = tokenRepo.findById(tokenValidation.createTokenValidationInformation(token));

        if (searchedToken.isEmpty()) {
            return null;
        }

        Token retrievedToken = searchedToken.get();

        if (tokenValidation.hasTokenExpired(Instant.now(), retrievedToken)) {
            tokenRepo.delete(retrievedToken);
            return null;
        }

        retrievedToken.setLast_used(Timestamp.from(Instant.now())); // to update tokenRollingTtl

        return tokenRepo.save(retrievedToken).getUser();

    }

    @Override
    public Integer createUser(String email, String password) throws UserCreationError {

        try {
            passwordValidation.isSafePassword(password);
        } catch (Exception e) {
            throw new UserCreationError.WeakPassword(); //todo communicate more specific
        }

        Optional<User> emailSearch = userRepo.findByEmail(email);

        if (emailSearch.isPresent()) {
            throw new UserCreationError.EmailInUse();
        }

        User created = userRepo.save(new User(
                new Region("Portugal"), //todo missing
                email,
                passwordValidation.passwordEncoding(password)
        ));

        return created.getId();
    }

    @Override
    public String login(String email, String password) throws UserAuthenticationError {

        Optional<User> searchedUser = userRepo.findByEmail(email);

        if (searchedUser.isEmpty()) {
            throw new UserAuthenticationError.UserOrPasswordAreInvalid();
        }

        User user = searchedUser.get();

        if (!passwordValidation.validatePassword(password, user.getPassword())) {
            throw new UserAuthenticationError.UserOrPasswordAreInvalid();
        }

        String token = tokenValidation.generateTokenValue();

        Token createdToken = new Token(tokenValidation.createTokenValidationInformation(token), searchedUser.get());

        tokenRepo.save(createdToken);

        return token;

    }

    @Override
    public void logout(String token) throws LogoutError {
        if (!tokenValidation.canBeToken(token)) {
            throw new LogoutError.TokenInvalidForAuthentication();
        }

        Optional<Token> searchedToken = tokenRepo.findById(tokenValidation.createTokenValidationInformation(token));

        if (searchedToken.isEmpty()) {
            throw new LogoutError.TokenInvalidForAuthentication();
        }

        tokenRepo.delete(searchedToken.get());

    }

    @Override
    public Owned addOwner(String isbn, String token) throws OwnerShipAdditionError {

        User user = checkAuthentication(token);

        if (user == null) {
            throw new OwnerShipAdditionError.UserAuthenticationInvalid();
        }

        boolean isIsbn10 = isbn.length() == 10;

        Book searchedBook;

        if (isIsbn10) {
            searchedBook = bookRepo.findByIsbnTen(isbn);
        } else {
            searchedBook = bookRepo.findByIsbnThirteen(isbn);
        }

        if (searchedBook == null) {
            throw new OwnerShipAdditionError.BookNotFound();
        }

        Owned toInsert = new Owned(user, searchedBook);

        if (ownedRepo.findById(toInsert.getId()).isPresent()) {
            throw new OwnerShipAdditionError.AlreadyMarkedAsOwned();
        }

        return ownedRepo.save(toInsert);

    }


}
