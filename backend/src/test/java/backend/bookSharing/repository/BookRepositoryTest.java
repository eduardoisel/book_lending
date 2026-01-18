package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.Book;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
@DataJpaTest
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class BookRepositoryTest{

    @Container
    @ServiceConnection
    static PostgreSQLContainer container = new PostgreSQLContainer("postgres:18-bookworm");

    @Autowired
    private BookRepository bookRepository;


    /*
    Tests both creation and search at the same time unfortunately
     */
    @Test
    public void createAndSearchTest(){
        Book b = new Book(123456789, null, "test", Book.Language.Portuguese);

        assertEquals(0, bookRepository.count());

        bookRepository.save(b);

        assertEquals(1, bookRepository.count());

    }

    @Test
    public void deletionTest(){
        Book temporaryInsert = new Book(123456789, null, "test", Book.Language.Portuguese);

        bookRepository.save(temporaryInsert);

        bookRepository.delete(temporaryInsert);
        List<Book> as = bookRepository.findAll();
        assertEquals(0, bookRepository.count());
    }

    @Test
    public void findByIsbn10Test(){
        Integer isbn10 = 123456789;

        Book inserted = new Book(isbn10, null, "test", Book.Language.Portuguese);

        bookRepository.save(inserted);

        Book found = bookRepository.findByIsbnTen(isbn10);

        assertNotNull(found);

        assertEquals(inserted.getIsbnTen(), found.getIsbnTen());
        assertEquals(inserted.getIsbnThirteen(), found.getIsbnThirteen());
        assertEquals(inserted.getTitle(), found.getTitle());
        assertEquals(inserted.getLanguage(), found.getLanguage());

    }

    @Test
    public void findByIsbn13Test(){
        Long isbn13 = 1234567890L;

        Book inserted = new Book(null, isbn13, "test", Book.Language.Portuguese);

        bookRepository.save(inserted);

        Book found = bookRepository.findByIsbnThirteen(isbn13);

        assertNotNull(found);

        assertEquals(inserted.getIsbnTen(), found.getIsbnTen());
        assertEquals(inserted.getIsbnThirteen(), found.getIsbnThirteen());
        assertEquals(inserted.getTitle(), found.getTitle());
        assertEquals(inserted.getLanguage(), found.getLanguage());

    }

}
