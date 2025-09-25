package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

//@Component
public interface BookRepository extends CrudRepository<Book, Integer> {

    Book findByIsbn10(Integer isbn_10);
}
