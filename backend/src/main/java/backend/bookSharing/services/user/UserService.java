package backend.bookSharing.services.user;

import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.Request;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.failures.LogoutError;
import backend.bookSharing.services.user.failures.OwnerShipAdditionError;
import backend.bookSharing.services.user.failures.OwnershipRequestSearchError;
import backend.bookSharing.services.user.failures.UserAuthenticationError;
import backend.bookSharing.services.user.failures.UserCreationError;
import backend.bookSharing.services.user.failures.UserOwnershipSearchError;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Actions related to a user (e.g. authentication, creation and updates of user, searches about users or books of user)
 */
public interface UserService {

    @Transactional
    Page<Book> getOwnedBooks(Integer userId, Integer pageNumber) throws UserOwnershipSearchError;

    @Transactional
    Page<Request> getRequestsOfBook(Integer ownerId, Integer bookId, Integer pageNumber) throws OwnershipRequestSearchError;

    /**
     * @return a {@link User} that may be null. Is specifically not either to avoid rollback on failure, as on failure
     * token is to be removed unless it did not exist in the first place
     */
    @Transactional
    @Nullable User checkAuthentication(String token);

    /**
     * @param email identifying email of {@link User}
     * @param password password of {@link User}
     * @return id of user
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    Integer createUser(String email, String password) throws UserCreationError;

    /**
     * Authentication through authentication token
     * @param email identifying email of {@link User}
     * @param password password of {@link User}
     * @return login token
     */
    @Transactional
    String login(String email, String password) throws UserAuthenticationError;

    /**
     * @param token authentication token of {@link User} logging out
     */
    @Transactional
    void logout(String token) throws LogoutError;

    /**
     *
     * @param isbn isbn 10 or 13 from book
     * @param token authentication token of {@link User} doing action
     * @return created {@link Owned} instance. Can be changed to void
     */
    @Transactional
    Owned addOwner(String isbn, String token) throws OwnerShipAdditionError;


}
