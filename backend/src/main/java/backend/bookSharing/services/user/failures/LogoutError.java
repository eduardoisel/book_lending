package backend.bookSharing.services.user.failures;

public sealed abstract class LogoutError extends Exception permits LogoutError.TokenInvalidForAuthentication{

    private LogoutError(){}

    public static final class TokenInvalidForAuthentication extends LogoutError {};
}
