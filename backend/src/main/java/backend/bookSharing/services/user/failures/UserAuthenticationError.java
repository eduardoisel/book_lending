package backend.bookSharing.services.user.failures;

public abstract sealed class UserAuthenticationError extends Exception
        permits UserAuthenticationError.UserOrPasswordAreInvalid {

    private UserAuthenticationError() {}

    public static final class UserOrPasswordAreInvalid extends UserAuthenticationError {}
}
