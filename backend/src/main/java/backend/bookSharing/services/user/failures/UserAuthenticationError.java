package backend.bookSharing.services.user.failures;

public class UserAuthenticationError extends Exception {

    private UserAuthenticationError(){}

    public static final class UserOrPasswordAreInvalid extends UserAuthenticationError {};

}
