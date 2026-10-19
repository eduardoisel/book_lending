package backend.bookSharing.services.book.failures;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract sealed class BookOwnersSearchError extends ServiceException
        permits BookOwnersSearchError.BookNotFound {

    private BookOwnersSearchError() {}

    /**
     * Book does not exist on database
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static final class BookNotFound extends BookOwnersSearchError {}
}
