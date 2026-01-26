package backend.bookSharing.services.user.failures;

public class UserCreationError extends Exception{

    /**
     * Password is not safe enough. Exact requirements to be defined
     */
    public static final class WeakPassword extends UserCreationError {};

    /**
     * Email can only be attached to one user
     */
    public static final class EmailInUse extends UserCreationError {};
}
