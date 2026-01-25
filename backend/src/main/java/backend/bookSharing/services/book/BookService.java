package backend.bookSharing.services.book;

import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.User;
import io.vavr.control.Either;
import java.util.List;
import java.util.Optional;
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
    Optional<BookAdditionError> addBookFromApi(String isbn);

}
