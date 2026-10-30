package backend.bookSharing.services.user.failures;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract sealed class UserCreationError extends ServiceException
        permits UserCreationError.EmailInUse {

    private UserCreationError() {}

    /**
     * Email already attached to one user
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class EmailInUse extends UserCreationError {}
}
