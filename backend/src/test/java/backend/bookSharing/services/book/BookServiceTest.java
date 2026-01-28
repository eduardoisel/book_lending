package backend.bookSharing.services.book;

import backend.bookSharing.ServiceTestBase;
import backend.bookSharing.TestData;
import backend.bookSharing.services.book.failures.BookAdditionError;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BookServiceTest extends ServiceTestBase {

    @Autowired
    private BookService service;

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

        try {
            service.addBookFromApi(TestData.booksExclusiveFromApi[0].getIsbnTen());
        } catch (BookAdditionError e) {
            fail("Should succeed");
        }

        assertThrowsExactly(
                BookAdditionError.Isbn10InUse.class,
                () -> service.addBookFromApi(TestData.databaseBooks[0].getIsbnTen())
        );

        assertThrowsExactly(
                BookAdditionError.Isbn13InUse.class,
                () -> service.addBookFromApi(TestData.databaseBooks[0].getIsbnTen())
        );

    }


}
