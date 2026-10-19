package backend.bookSharing.services.book.failures;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract sealed class BookRequestError extends ServiceException
        permits BookRequestError.OwnershipNotFound,
                BookRequestError.AlreadyRequested,
                BookRequestError.CannotRequestFromSelf {

    private BookRequestError() {}

    /**
     * Requesting book from self is not allowed
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class CannotRequestFromSelf extends BookRequestError {}

    /**
     * Target user does not own book
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class OwnershipNotFound extends BookRequestError {}

    /**
     * Current book from user was already requested
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class AlreadyRequested extends BookRequestError {}
}
