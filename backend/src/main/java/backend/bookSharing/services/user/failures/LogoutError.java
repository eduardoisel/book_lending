package backend.bookSharing.services.user.failures;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract sealed class LogoutError extends ServiceException
        permits LogoutError.TokenInvalidForAuthentication {

    private LogoutError() {}

    /**
     * Token may either not exist or even be of a valid format
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class TokenInvalidForAuthentication extends LogoutError {}
    ;
}
