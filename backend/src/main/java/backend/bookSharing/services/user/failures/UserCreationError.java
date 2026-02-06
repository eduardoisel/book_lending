package backend.bookSharing.services.user.failures;

public abstract sealed class UserCreationError extends Exception permits UserCreationError.EmailInUse, UserCreationError.WeakPassword{

    private UserCreationError(){}

    private UserCreationError(String message){
        super(message);
    }

    /**
     * Password is not safe enough. Exact requirements to be defined
     */
    public static final class WeakPassword extends UserCreationError {
        public WeakPassword(String password){
            super(String.format("Password %s is considered weak", password));
        }
    }

    /**
     * Email can only be attached to one user
     */
    public static final class EmailInUse extends UserCreationError {}
}
