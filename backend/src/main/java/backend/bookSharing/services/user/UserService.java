package backend.bookSharing.services.user;

import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Lend;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.Request;
import backend.bookSharing.repository.entities.User;
import com.sun.istack.NotNull;
import io.vavr.control.Either;
import jakarta.annotation.Nullable;
import java.util.List;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Actions related to a user (e.g. authentication, creation and updates of user, searches about users or books of user)
 *
 * Note: if {@link org.springframework.lang.NonNullApi} annotation package level is not working, all objects given and received are to be assumed that
 * cannot be null
 */
public interface UserService {

    public long bookCount();

    @Transactional
    public List<Book> getOwnedBooks(Integer userId);

    @Transactional
    public List<Request> getRequestsOfBook(Integer ownerId, Integer bookId);

    @Transactional
    public Lend getLendOfBook(Integer ownerId, Integer bookId);

    /**
     * @return a {@link User} that may be null. Is specifically not either to avoid rollback on failure, as on failure
     * token is to be removed unless it did not exist in the first place
     */
    @Transactional
    @Nullable
    public User checkAuthentication(String token);

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Either<UserCreationError, Integer> createUser(String email, @NotNull String password);

    @Transactional
    public Either<UserAuthenticationError, String> login(String email, String password);

    @Transactional
    public Either<LogoutError, Void> logout(String token);

    /**
     * TEMPORARY, no auth
     *
     * @param isbn
     * @return
     */
    @Transactional
    Either<OwnerShipAdditionError,Owned> addOwner(String isbn, String token);


}
