package backend.bookSharing.services.book;

import backend.bookSharing.RandomValuesGenerator;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.services.ServiceTestBase;
import backend.bookSharing.TestData;
import backend.bookSharing.services.book.failures.BookAdditionError;


import backend.bookSharing.services.book.failures.BookOwnersSearchError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration
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
    public void addRepeatedBookToDatabase() {

        Book book = TestData.databaseBooks[0];

        assertThrowsExactly(
                BookAdditionError.Isbn10InUse.class,
                () -> service.addBookFromApi(book.getIsbnTen())
        );

        assertThrowsExactly(
                BookAdditionError.Isbn13InUse.class,
                () -> service.addBookFromApi(book.getIsbnThirteen())
        );

    }

    @Test
    public void addBookNotInAPI() {

        Book book = new Book(
                RandomValuesGenerator.generateNumeric(10),
                RandomValuesGenerator.generateNumeric(13),
                RandomValuesGenerator.generateAlphaNumeric(5),
                Book.Language.English);

        assertThrowsExactly(
                BookAdditionError.BookNotFound.class,
                () -> service.addBookFromApi(book.getIsbnTen())
        );

    }

    @Test
    public void searchOwnersOfNonExistentBook() {

        Book book = new Book(
                RandomValuesGenerator.generateNumeric(10),
                RandomValuesGenerator.generateNumeric(13),
                RandomValuesGenerator.generateAlphaNumeric(5),
                Book.Language.English);

        assertThrowsExactly(
                BookOwnersSearchError.BookNotFound.class,
                () -> service.getOwnersOfBook(book.getIsbnTen(), 0)
        );

        assertThrowsExactly(
                BookOwnersSearchError.BookNotFound.class,
                () -> service.getOwnersOfBook(book.getIsbnThirteen(), 0)
        );
    }

}
