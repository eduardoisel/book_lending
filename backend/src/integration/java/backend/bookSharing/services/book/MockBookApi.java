package backend.bookSharing.services.book;

import backend.bookSharing.TestData;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.services.book.api.BookApi;
import java.util.Arrays;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public class MockBookApi implements BookApi {

    @Override
    @Nullable
    public Book getBook(String isbn) {

        if (isbn == null) {
            throw new IllegalStateException("Should not be null");
        }

        if (isbn.length() == 13) {
            Optional<Book> b = Arrays.stream(TestData.allBooks)
                    .filter(book -> {
                        String isbn13 = book.getIsbnThirteen();

                        if (isbn13 == null){
                            return false;
                        }

                        return isbn13.equals(isbn);
                    }).findFirst();

            if (b.isEmpty()){
                throw new RuntimeException("Mock api has not found the book");
            }

            return b.get();

        }

        Optional<Book> b = Arrays.stream(TestData.allBooks)
                .filter(book -> {
                    String isbn10 = book.getIsbnTen();
                    if (isbn10 == null){
                        return false;
                    }
                    return book.getIsbnTen().equals(isbn);
                }).findFirst();

        return b.map(TestData::duplicate).orElse(null);

    }
}
