package backend.bookSharing.services.user;

public class UserAuthenticationError {

    private UserAuthenticationError(){}

    public static final class UserOrPasswordAreInvalid extends UserAuthenticationError {};

}
