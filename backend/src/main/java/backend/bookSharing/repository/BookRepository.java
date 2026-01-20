package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//import org.springframework.data.repository.query.ValueExpressionDelegate

@EnableJpaRepositories
/*
  Automatically implemented

  Extra functions seem to work only if fields is book are changed to not have "_"
 */
public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findByIsbnTen(String  isbn_10);

    Book findByIsbnThirteen(String isbn_13);

    //List<Book> findByLanguage(Language language);
}
