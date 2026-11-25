package backend.bookSharing.services.book.failures;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when trying to add book information
 */
public abstract sealed class BookAdditionError extends ServiceException
        permits BookAdditionError.IsbnInUse, BookAdditionError.BookNotFound {

    private BookAdditionError() {}

    /**
     * Was given an ISBN identifier already in database
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class IsbnInUse extends BookAdditionError {}

    /**
     * Book was not found to exist from given ISBN
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static final class BookNotFound extends BookAdditionError {}
}
