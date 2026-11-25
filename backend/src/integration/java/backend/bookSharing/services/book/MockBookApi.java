package backend.bookSharing.services.book;

import backend.bookSharing.TestData;
import backend.bookSharing.data.TestBook;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.services.book.api.BookApi;
import java.util.Arrays;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public class MockBookApi implements BookApi {

    /*
    Always return duplicate
     */
    @Override
    @Nullable public Book getBook(String isbn) {

        if (isbn == null) {
            throw new IllegalStateException("Should not be null");
        }

        if (isbn.length() != 13) {
            throw new IllegalStateException(
                    "ISBN length should be 13. Exception placed as service layer uses only ISBN 13"
                            + " now.");
        }

        Optional<TestBook> searchedIsbn13Book =
                Arrays.stream(TestData.allBooks)
                        .filter(
                                book -> {
                                    String isbn13 = book.getIsbnThirteen();

                                    return isbn13.equals(isbn);
                                })
                        .findFirst();

        return searchedIsbn13Book.map(TestData::duplicate).orElse(null);
    }
}
