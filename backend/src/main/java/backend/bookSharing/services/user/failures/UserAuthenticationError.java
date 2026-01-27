package backend.bookSharing.services.user.failures;

public sealed abstract class UserAuthenticationError extends Exception permits UserAuthenticationError.UserOrPasswordAreInvalid {

    private UserAuthenticationError(){}

    public static final class UserOrPasswordAreInvalid extends UserAuthenticationError {}

}
