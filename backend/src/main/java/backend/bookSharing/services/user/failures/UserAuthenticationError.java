package backend.bookSharing.services.user.failures;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract sealed class UserAuthenticationError extends ServiceException
        permits UserAuthenticationError.UserOrPasswordAreInvalid {

    private UserAuthenticationError() {}

    /**
     * User or password invalid
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class UserOrPasswordAreInvalid extends UserAuthenticationError {}
}
