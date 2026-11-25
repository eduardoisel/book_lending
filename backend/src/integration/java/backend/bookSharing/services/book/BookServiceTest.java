package backend.bookSharing.services.book;

import static org.junit.jupiter.api.Assertions.*;

import backend.bookSharing.RandomValuesGenerator;
import backend.bookSharing.TestData;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.services.ServiceTestBase;
import backend.bookSharing.services.book.failures.BookAdditionError;
import backend.bookSharing.services.book.failures.BookOwnersSearchError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

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
            service.addBookFromApi(TestData.booksExclusiveFromApi[0].getIsbnThirteen());
        } catch (BookAdditionError e) {
            fail("Should succeed");
        }
    }

    @Test
    public void addRepeatedBookToDatabase() {

        Book book = TestData.databaseBooks[0];

        assertThrowsExactly(
                BookAdditionError.IsbnInUse.class,
                () -> service.addBookFromApi(book.getIsbnThirteen()));
    }

    @Test
    public void addBookNotInAPI() {

        Book book =
                new Book(
                        RandomValuesGenerator.generateNumeric(13),
                        RandomValuesGenerator.generateAlphaNumeric(5),
                        Book.Language.English);

        assertThrowsExactly(
                BookAdditionError.BookNotFound.class,
                () -> service.addBookFromApi(book.getIsbnThirteen()));
    }

    @Test
    public void searchOwnersOfNonExistentBook() {

        Book book =
                new Book(
                        RandomValuesGenerator.generateNumeric(13),
                        RandomValuesGenerator.generateAlphaNumeric(5),
                        Book.Language.English);

        assertThrowsExactly(
                BookOwnersSearchError.BookNotFound.class,
                () ->
                        service.getOwnersOfBook(
                                book.getIsbnThirteen(), 0, TestData.points.getFirst()));

        assertThrowsExactly(
                BookOwnersSearchError.BookNotFound.class,
                () ->
                        service.getOwnersOfBook(
                                book.getIsbnThirteen(), 0, TestData.points.getFirst()));
    }
}
