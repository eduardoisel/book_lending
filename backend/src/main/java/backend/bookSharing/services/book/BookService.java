package backend.bookSharing.services.book;

import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.book.failures.BookAdditionError;
import backend.bookSharing.services.book.failures.BookLendError;
import backend.bookSharing.services.book.failures.BookOwnersSearchError;
import backend.bookSharing.services.book.failures.BookRequestError;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Actions on data related to books, such as addition of books, general searches of books and lists of users who own
 * specific books
 */
public interface BookService {

    @Transactional
    Page<Book> getBooks(Integer pageNumber);

    /**
     *
     * @param isbn13 ISBN 13 of requested {@link Book}
     * @param pageNumber the current page requested. smallest is 0
     * @param location location of user doing the request
     * @return List of users who own the selected book. Limited to users deemed to be close enough
     */
    @Transactional
    Page<User> getOwnersOfBook(String isbn13, Integer pageNumber, Point location)
            throws BookOwnersSearchError;

    /**
     * @param isbn13 ISBN 13 of requested {@link Book}
     * @return book added to the api
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    Book addBookFromApi(String isbn13) throws BookAdditionError;

    /**
     * @param isbn13 ISBN 13 of requested {@link Book}
     * @param ownerEmail email of owner, a {@link User}
     * @param user authenticated {@link User} doing request
     * @param timeInDays amount of days requested
     */
    @Transactional
    void requestBook(String isbn13, String ownerEmail, User user, Integer timeInDays)
            throws BookRequestError;

    /**
     * @param isbn13 ISBN 13 of requested {@link Book}
     * @param receiverEmail email of receiver
     * @param user authenticated {@link  User}
     */
    @Transactional
    void lendBook(String isbn13, String receiverEmail, User user) throws BookLendError;
}
