package backend.bookSharing.services.user;

import backend.bookSharing.repository.RegionRepository;
import backend.bookSharing.repository.UserRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Lend;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.Region;
import backend.bookSharing.repository.entities.Request;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.services.PasswordValidation;
import io.vavr.control.Either;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    private final RegionRepository regionRepo;

    private final PasswordValidation passwordValidation;

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
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    //@jakarta.transaction.Transactional
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
                new Region("Portugal"),
                email,
                passwordValidation.passwordEncoding(password)
        ));

        return Either.right(created.getId());
    }

    @Override
    public Either<UserAuthenticationError, String> login(String email, String password) {
        return null;
    }

    @Override
    public Optional<LogoutError> logout(String token) {
        return Optional.empty();
    }


}
