package backend.bookSharing.services.user.failures;

public class LogoutError extends Exception {
    public static final class TokenInvalidForAuthentication extends LogoutError {};
}
