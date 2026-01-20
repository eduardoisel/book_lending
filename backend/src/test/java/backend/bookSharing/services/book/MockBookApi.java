package backend.bookSharing.services.book;

import backend.bookSharing.TestData;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.services.book.api.BookApi;
import java.util.Arrays;
import java.util.Optional;

public class MockBookApi implements BookApi {

    @Override
    public Book getBook(String isbn) {

        if (isbn == null) {
            throw new IllegalStateException("Should not be null");
        }

        if (isbn.length() == 13) {
            Optional<Book> b = Arrays.stream(TestData.allBooks)
                    .filter(book -> book.getIsbnThirteen().equals(isbn)).findFirst();

            if (b.isEmpty()){
                throw new RuntimeException("Mock api has not found the book");
            }

            return b.get();

        }

        Optional<Book> b = Arrays.stream(TestData.allBooks)
                .filter(book -> book.getIsbnTen().equals(isbn)).findFirst();

        if (b.isEmpty()){
            throw new RuntimeException("Mock api has not found the book");
        }

        return b.get();

    }
}
