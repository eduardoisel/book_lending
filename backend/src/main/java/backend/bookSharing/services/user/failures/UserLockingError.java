package backend.bookSharing.services.user.failures;

public abstract sealed class UserLockingError extends Exception
        permits UserLockingError.UserDoesNotExist {

    private UserLockingError() {}

    public static final class UserDoesNotExist extends UserLockingError {}
    ;
}
