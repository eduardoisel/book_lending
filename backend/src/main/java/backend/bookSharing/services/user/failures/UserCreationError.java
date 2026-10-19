package backend.bookSharing.services.user.failures;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract sealed class UserCreationError extends ServiceException
        permits UserCreationError.EmailInUse,
                UserCreationError.WeakPassword,
                UserCreationError.InvalidLatitude,
                UserCreationError.InvalidLongitude {

    private UserCreationError() {}

    // most likely to be replaced by multiple subclasses
    /**
     * Password is not safe enough. Exact requirements to be communicated better
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class WeakPassword extends UserCreationError {}

    /**
     * Email already attached to one user
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class EmailInUse extends UserCreationError {}

    /**
     * Given latitude is not possible
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class InvalidLatitude extends UserCreationError {}

    /**
     * Given longitude is not possible
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class InvalidLongitude extends UserCreationError {}
}
