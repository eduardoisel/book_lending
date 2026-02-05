package backend.bookSharing.services.book.api;

import backend.bookSharing.repository.entities.Book;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface BookApi {

    /**
     *
     * @param isbn, HAS TO BE 10 or 13 digits, as these are the only acceptable isbn numbers
     *
     * @return Book information from the API
     */
    @Nullable
    Book getBook(@NonNull String isbn);
}
