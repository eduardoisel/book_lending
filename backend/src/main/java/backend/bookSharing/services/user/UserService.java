package backend.bookSharing.services.user;

import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Lend;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.Request;
import com.sun.istack.NotNull;
import io.vavr.control.Either;
import java.util.List;
import java.util.Optional;
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
    Optional<Owned> addOwner(String isbn, String token);


}
