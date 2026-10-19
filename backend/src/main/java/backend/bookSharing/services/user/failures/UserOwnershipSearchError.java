package backend.bookSharing.services.user.failures;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract sealed class UserOwnershipSearchError extends ServiceException
        permits UserOwnershipSearchError.UserDoesNotExist {

    private UserOwnershipSearchError() {}

    /**
     * User in question does not exist
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static final class UserDoesNotExist extends UserOwnershipSearchError {}
}
