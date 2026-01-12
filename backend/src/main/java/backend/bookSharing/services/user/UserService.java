package backend.bookSharing.services.user;

import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Lend;
import backend.bookSharing.repository.entities.Request;
import io.vavr.control.Either;
import java.util.List;

public interface UserService {

    public long bookCount();

    public List<Book> getOwnedBooks(Integer userId);

    public List<Request> getRequestsOfBook(Integer ownerId, Integer bookId);

    public Lend getLendOfBook(Integer ownerId, Integer bookId);


    public Either<UserCreationError, Integer> createUser(String email, String password);
}
