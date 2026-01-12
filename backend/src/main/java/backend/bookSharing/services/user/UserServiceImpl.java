package backend.bookSharing.services.user;

import backend.bookSharing.repository.UserRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Lend;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.Request;
import io.vavr.control.Either;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private final UserRepository repo;

    @PersistenceContext private EntityManager em;

    public UserServiceImpl(UserRepository userRepository){
        this.repo = userRepository;
    }

    public long bookCount(){
        return repo.count();
    }

    @Override
    public List<Book> getOwnedBooks(Integer userId){
        return repo.getReferenceById(userId).getOwned().stream().map(Owned::getBook).toList();
    }

    @Override
    public List<Request> getRequestsOfBook(Integer ownerId, Integer bookId){
        return repo.getReferenceById(ownerId).getOwned()
                .stream().filter(owned -> owned.getBook().getId().equals(bookId)).toList().getFirst()
                .getRequests().stream().toList();
    }

    @Override
    public Lend getLendOfBook(Integer ownerId, Integer bookId){
        return repo.getReferenceById(ownerId).getOwned()
                .stream().filter(owned -> owned.getBook().getId().equals(bookId)).toList().getFirst()
                .getLend();
    }

    @Override
    @Transactional(transactionManager = "aa", isolation = Isolation.REPEATABLE_READ)
    //@jakarta.transaction.Transactional
    public Either<UserCreationError, Integer> createUser(String email, String password) {

        return null;
    }


}
