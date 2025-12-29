package backend.bookSharing.services;

import backend.bookSharing.repository.UserRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Lend;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.Request;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private final UserRepository repo;

    @PersistenceContext private EntityManager em;

    public UserService(UserRepository userRepository){
        this.repo = userRepository;
    }

    public long bookCount(){
        return repo.count();
    }

    public List<Book> getOwnedBooks(Integer userId){
        return repo.getReferenceById(userId).getOwned().stream().map(Owned::getBook).toList();
    }

    public List<Request> getRequestsOfBook(Integer ownerId, Integer bookId){
        return repo.getReferenceById(ownerId).getOwned()
                .stream().filter(owned -> owned.getBook().getId().equals(bookId)).toList().getFirst()
                .getRequests().stream().toList();
    }

    public Lend getLendOfBook(Integer ownerId, Integer bookId){
        return repo.getReferenceById(ownerId).getOwned()
                .stream().filter(owned -> owned.getBook().getId().equals(bookId)).toList().getFirst()
                .getLend();
    }


}
