package backend.bookSharing.services;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private final BookRepository repo;

    public BookService(BookRepository bookRepository){
        this.repo = bookRepository;
    }

    public long bookCount(){
        return repo.count();
    }

    public List<User> getOwnersOfBook(Integer bookId){
        return repo.getReferenceById(bookId).getOwners();
    }

}
