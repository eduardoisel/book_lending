package backend.bookSharing.data;

import backend.bookSharing.repository.entities.Book;
import jakarta.annotation.Nullable;
import lombok.Getter;

/**
 * Extends the base book. Base does not contain ISBN 10 due to it being possible to convert to IABN 13. However, as the
 * API accepts ISBN 10 as a possible value, saving it can be useful for testing.
 */
public class TestBook extends Book {

    @Nullable @Getter public String isbnTen;

    public TestBook(@Nullable String isbn10, String isbn_13, String title, Language language) {
        super(isbn_13, title, language);
        this.isbnTen = isbn10;
    }
}
