package backend.bookSharing.services.book.failures;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract sealed class BookLendError extends ServiceException
        permits BookLendError.RequestNotFound, BookLendError.AlreadyLent {

    private BookLendError() {}

    /**
     * Identified request does not exist
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static final class RequestNotFound extends BookLendError {}

    /**
     * A book can only be lent if it is not being currently lent to another
     *
     * Note: this is more restrictive than database model, which allows for multiple lends of the same book.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static final class AlreadyLent extends BookLendError {}
}
