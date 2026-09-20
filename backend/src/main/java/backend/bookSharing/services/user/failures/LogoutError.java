package backend.bookSharing.services.user.failures;

public abstract sealed class LogoutError extends Exception
        permits LogoutError.TokenInvalidForAuthentication {

    private LogoutError() {}

    public static final class TokenInvalidForAuthentication extends LogoutError {}
    ;
}
