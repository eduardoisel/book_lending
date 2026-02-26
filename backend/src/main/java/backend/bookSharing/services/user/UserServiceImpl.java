package backend.bookSharing.services.user;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.LendRepository;
import backend.bookSharing.repository.OwnedRepository;
import backend.bookSharing.repository.RequestRepository;
import backend.bookSharing.repository.TokenRepository;
import backend.bookSharing.repository.UserRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.OwnedId;
import backend.bookSharing.repository.entities.Region;
import backend.bookSharing.repository.entities.Request;
import backend.bookSharing.repository.entities.Token;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.failures.LogoutError;
import backend.bookSharing.services.user.failures.OwnerShipAdditionError;
import backend.bookSharing.services.user.failures.OwnershipRequestSearchError;
import backend.bookSharing.services.user.failures.UserAuthenticationError;
import backend.bookSharing.services.user.failures.UserCreationError;
import backend.bookSharing.services.user.failures.UserOwnershipSearchError;
import backend.bookSharing.services.user.services.PasswordValidation;
import backend.bookSharing.services.user.services.TokenValidation;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    private final OwnedRepository ownedRepo;

    private final BookRepository bookRepo;

    private final TokenRepository tokenRepo;

    private final LendRepository lendRepo;

    private final RequestRepository requestRepo;

    private final PasswordValidation passwordValidation;

    private final TokenValidation tokenValidation;

    @Override
    public Page<Book> getOwnedBooks(Integer userId, Integer pageNumber) throws UserOwnershipSearchError {

        if (!userRepo.existsById(userId)) {
            throw new UserOwnershipSearchError.UserDoesNotExist();
        }

        return ownedRepo.findByUserId(userId, PageRequest.of(
                pageNumber,
                20
        )).map(Owned::getBook);

    }

    @Override
    public Page<Request> getRequestsOfBook(Integer ownerId, Integer bookId, Integer pageNumber) throws OwnershipRequestSearchError {

        if (!ownedRepo.existsById(new OwnedId(ownerId, bookId))) {
            throw new OwnershipRequestSearchError.OwnershipDoesNotExist();
        }

        return requestRepo.findByOwnedId(new OwnedId(ownerId, bookId), PageRequest.of(
                pageNumber,
                20 //, Sort.by("book_id")
        ));

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

        retrievedToken.setLastUsed(Timestamp.from(Instant.now())); // to update tokenRollingTtl

        return tokenRepo.save(retrievedToken).getUser();

    }

    @Override
    public Integer createUser(String email, String password) throws UserCreationError {

        try {
            passwordValidation.isSafePassword(password);
        } catch (Exception e) {
            throw new UserCreationError.WeakPassword(password); //todo communicate more specific
        }

        Optional<User> emailSearch = userRepo.findByEmail(email);

        if (emailSearch.isPresent()) {
            throw new UserCreationError.EmailInUse();
        }

        String salt = passwordValidation.getSalt();

        User created = userRepo.save(new User(
                new Region("Portugal"), //todo missing
                email,
                passwordValidation.passwordEncoding(password, salt),
                salt
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

        if (!passwordValidation.validatePassword(password, user.getValidationInfo())) {
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
    public Owned addOwner(String isbn, User user) throws OwnerShipAdditionError {

        Book searchedBook = isbn.length() == 10 ? bookRepo.findByIsbnTen(isbn) : bookRepo.findByIsbnThirteen(isbn);

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
