package backend.bookSharing.services.user;

import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Lend;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.Request;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.failures.LogoutError;
import backend.bookSharing.services.user.failures.OwnerShipAdditionError;
import backend.bookSharing.services.user.failures.UserAuthenticationError;
import backend.bookSharing.services.user.failures.UserCreationError;
import com.sun.istack.NotNull;
import jakarta.annotation.Nullable;
import java.util.List;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Actions related to a user (e.g. authentication, creation and updates of user, searches about users or books of user)
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
    public Integer createUser(String email, @NotNull String password) throws UserCreationError;

    @Transactional
    public String login(String email, String password) throws UserAuthenticationError;

    @Transactional
    public void logout(String token) throws LogoutError;

    /**
     *
     * @param isbn
     * @return
     */
    @Transactional
    Owned addOwner(String isbn, String token) throws OwnerShipAdditionError;


}
