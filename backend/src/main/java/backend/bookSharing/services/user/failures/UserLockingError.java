package backend.bookSharing.services.user.failures;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract sealed class UserLockingError extends ServiceException
        permits UserLockingError.UserDoesNotExist {

    private UserLockingError() {}

    /**
     * User in question does not exist
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static final class UserDoesNotExist extends UserLockingError {}
}
