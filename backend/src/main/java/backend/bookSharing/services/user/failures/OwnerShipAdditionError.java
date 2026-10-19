package backend.bookSharing.services.user.failures;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract sealed class OwnerShipAdditionError extends ServiceException
        permits OwnerShipAdditionError.AlreadyMarkedAsOwned, OwnerShipAdditionError.BookNotFound {

    private OwnerShipAdditionError() {}

    /**
     * User in question already owns book in question
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static final class AlreadyMarkedAsOwned extends OwnerShipAdditionError {}

    /**
     * Book in question does not exist in the server
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static final class BookNotFound extends OwnerShipAdditionError {}
}
