package backend.bookSharing.services.book;

import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.services.ServiceTestBase;
import backend.bookSharing.TestData;
import backend.bookSharing.services.book.failures.BookAdditionError;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Rollback(value = true)
public class BookServiceTest extends ServiceTestBase {

    private final BookService service;

    @Autowired
    public BookServiceTest(BookService service) {
        this.service = service;
    }

    @Test
    public void addBookFromApiTest() {

        try {
            service.addBookFromApi(TestData.booksExclusiveFromApi[0].getIsbnTen());
        } catch (BookAdditionError e) {
            fail("Should succeed");
        }


    }

    @Test
    public void addRepeatedBookToApi() {

        Book book = TestData.booksExclusiveFromApi[0];

        try {
            service.addBookFromApi(book.getIsbnTen());
        } catch (BookAdditionError e) {
            fail("Should succeed");
        }

        assertThrowsExactly(
                BookAdditionError.Isbn10InUse.class,
                () -> service.addBookFromApi(book.getIsbnTen())
        );

        assertThrowsExactly(
                BookAdditionError.Isbn13InUse.class,
                () -> service.addBookFromApi(book.getIsbnThirteen())
        );

    }


}
