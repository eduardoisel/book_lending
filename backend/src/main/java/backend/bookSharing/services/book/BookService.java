package backend.bookSharing.services.book;

import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.book.failures.BookAdditionError;
import backend.bookSharing.services.book.failures.BookLendError;
import backend.bookSharing.services.book.failures.BookOwnersSearchError;
import backend.bookSharing.services.book.failures.BookRequestError;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Actions on data related to books, such as addition of books, general searches of books and lists of users who own
 * specific books
 */
public interface BookService {

    @Transactional
    Page<User> getOwnersOfBook(String isbn, Integer pageNumber) throws BookOwnersSearchError;

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
