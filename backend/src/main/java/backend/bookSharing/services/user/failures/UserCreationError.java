package backend.bookSharing.services.user.failures;

public abstract sealed class UserCreationError extends Exception permits UserCreationError.EmailInUse, UserCreationError.WeakPassword{

    private UserCreationError(){}

    /**
     * Password is not safe enough. Exact requirements to be defined
     */
    public static final class WeakPassword extends UserCreationError {};

    /**
     * Email can only be attached to one user
     */
    public static final class EmailInUse extends UserCreationError {};
}
