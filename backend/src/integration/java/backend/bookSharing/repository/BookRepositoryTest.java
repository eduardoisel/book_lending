package backend.bookSharing.repository;

import static org.junit.jupiter.api.Assertions.*;

import backend.bookSharing.TestData;
import backend.bookSharing.repository.entities.Book;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BookRepositoryTest extends DatabaseTest {

    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryTest(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    public void createAndSearchTest() {
        Book b = TestData.duplicate(TestData.databaseBooks[0]);

        assertEquals(0, bookRepository.count());
        bookRepository.save(b);
        assertEquals(1, bookRepository.count());
    }

    @Test
    public void createWithBadIsbn13Test() {
        Book b = new Book("abcdefg", "irrelevant", Book.Language.English);

        assertThrowsExactly(ConstraintViolationException.class, () -> bookRepository.save(b));
    }

    @Test
    public void deletionTest() {
        Book temporaryInsert = TestData.duplicate(TestData.databaseBooks[0]);

        bookRepository.save(temporaryInsert);
        bookRepository.delete(temporaryInsert);
        assertEquals(0, bookRepository.count());
    }

    @Test
    public void findByIsbnTest() {
        Book inserted =
                TestData.duplicate(
                        TestData.databaseBooks[
                                0]); // new Book(null, isbn13, "test", Book.Language.Portuguese);

        String isbn13 = inserted.getIsbnThirteen();

        bookRepository.save(inserted);

        Book found = bookRepository.findByIsbnThirteen(isbn13);

        assertNotNull(found);

        assertEquals(inserted.getIsbnThirteen(), found.getIsbnThirteen());
        assertEquals(inserted.getTitle(), found.getTitle());
        assertEquals(inserted.getLanguage(), found.getLanguage());
    }
}
