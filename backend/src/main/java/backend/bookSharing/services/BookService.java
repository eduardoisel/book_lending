package backend.bookSharing.services;

import backend.bookSharing.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository bookRepository){
        this.repo = bookRepository;
    }

    public long bookCount(){
        return repo.count();
    }


}
