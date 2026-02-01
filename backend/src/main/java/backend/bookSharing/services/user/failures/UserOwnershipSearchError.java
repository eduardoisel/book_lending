package backend.bookSharing.services.user.failures;

public abstract sealed class UserOwnershipSearchError extends Exception permits UserOwnershipSearchError.UserDoesNotExist{

    private UserOwnershipSearchError(){}

    public static final class UserDoesNotExist extends UserOwnershipSearchError{}
}
