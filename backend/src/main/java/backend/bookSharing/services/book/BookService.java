package backend.bookSharing.services.book;

import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.book.failures.BookAdditionError;
import backend.bookSharing.services.book.failures.BookLendError;
import backend.bookSharing.services.book.failures.BookRequestError;
import java.util.List;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Actions on data related to books, such as addition of books, general searches of books and lists of users who own
 * specific books
 */
public interface BookService {

    long bookCount();

    @Transactional
    List<User> getOwnersOfBook(Integer bookId);

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    void addBookFromApi(String isbn) throws BookAdditionError;

    /**
     *
     * @param isbn isbn of requested {@link Book}
     * @param ownerEmail email of owner, a {@link User}
     * @param token auth token of {@link User} doing request
     * @param timeInDays amount of days requested
     */
    @Transactional
    void requestBook(String isbn, String ownerEmail, String token, Integer timeInDays) throws BookRequestError;

    @Transactional
    void lendBook(String isbn, String receiverEmail, String token) throws BookLendError;

}
