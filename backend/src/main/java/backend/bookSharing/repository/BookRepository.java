package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//import org.springframework.data.repository.query.ValueExpressionDelegate

@EnableJpaRepositories
public interface BookRepository extends JpaRepository<Book, Integer> {

    //Book findByIsbn10(Integer isbn_10);

    //List<Book> findByLanguage(Language language);
}
