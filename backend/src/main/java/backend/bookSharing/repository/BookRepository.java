package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.book.Book;
import backend.bookSharing.repository.entities.book.Language;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface BookRepository extends CrudRepository<Book, Integer> {

    //Book findByIsbn10(Integer isbn_10);

    //List<Book> findByLanguage(Language language);
}
