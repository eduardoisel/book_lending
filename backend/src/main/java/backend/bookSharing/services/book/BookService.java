package backend.bookSharing.services.book;

import backend.bookSharing.repository.entities.User;
import io.vavr.control.Either;
import java.util.List;
import java.util.Optional;

public interface BookService {

    long bookCount();

    List<User> getOwnersOfBook(Integer bookId);

    Optional<BookAdditionError> addBookFromApi(String isbn);

}
