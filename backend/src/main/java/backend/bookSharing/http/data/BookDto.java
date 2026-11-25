package backend.bookSharing.http.data;

import backend.bookSharing.utils.IsbnUtils;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;

/**
 * Information about a book
 * @param id unique identifier for the book.
 * @param isbn10 book's ISBN 10. May be null
 * @param isbn13 book's ISBN 13.
 * @param title The book's title
 * @param language language the book is written on
 */
public record BookDto(
        int id,
        @ISBN(type = ISBN.Type.ISBN_10) String isbn10,
        @NotNull @ISBN(type = ISBN.Type.ISBN_13) String isbn13,
        @NotNull String title, // @Size(min =100)
        @NotNull backend.bookSharing.repository.entities.Book.Language language) {
    public BookDto(backend.bookSharing.repository.entities.Book book) {
        this(
                book.getId(),
                IsbnUtils.isIsbn13ConvertibleTo10(book.getIsbnThirteen())
                        ? IsbnUtils.isbn13To10(book.getIsbnThirteen())
                        : null,
                book.getIsbnThirteen(),
                book.getTitle(),
                book.getLanguage());
    }
}
