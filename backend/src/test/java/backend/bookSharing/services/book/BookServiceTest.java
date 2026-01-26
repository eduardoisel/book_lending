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
    public void addBookFromApiTest(){

        Optional<BookAdditionError> success = service
                .addBookFromApi(TestData.booksExclusiveFromApi[0].getIsbnTen());

        assertTrue(success.isEmpty());

    }

    @Test
    public void addRepeatedBookToApi(){

        Optional<BookAdditionError> success = service
                .addBookFromApi(TestData.booksExclusiveFromApi[0].getIsbnTen());

        assertTrue(success.isEmpty());

        Optional<BookAdditionError> repeatIsbn10Entry = service
                .addBookFromApi(TestData.databaseBooks[0].getIsbnTen());

        assertFalse(repeatIsbn10Entry.isEmpty());

        assertInstanceOf(BookAdditionError.Isbn10InUse.class, repeatIsbn10Entry.get());

        Optional<BookAdditionError> repeatIsbn13Entry = service
                .addBookFromApi(TestData.databaseBooks[0].getIsbnThirteen());

        assertFalse(repeatIsbn13Entry.isEmpty());

        assertInstanceOf(BookAdditionError.Isbn13InUse.class, repeatIsbn13Entry.get());

    }


}
