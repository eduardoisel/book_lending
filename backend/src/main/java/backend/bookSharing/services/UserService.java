package backend.bookSharing.services;

import backend.bookSharing.repository.UserRepository;
import backend.bookSharing.repository.entities.Book;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private final UserRepository repo;

    public UserService(UserRepository userRepository){
        this.repo = userRepository;
    }

    public long bookCount(){
        return repo.count();
    }

    public List<Book> getOwnedBooks(Integer userId){
        return repo.getReferenceById(userId).getOwners();
    }

}
