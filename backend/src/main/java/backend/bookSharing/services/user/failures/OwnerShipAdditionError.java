package backend.bookSharing.services.user.failures;

public abstract sealed class OwnerShipAdditionError extends Exception
        permits OwnerShipAdditionError.AlreadyMarkedAsOwned, OwnerShipAdditionError.BookNotFound {

    private OwnerShipAdditionError() {}

    public static final class AlreadyMarkedAsOwned extends OwnerShipAdditionError {}
    ;

    public static final class BookNotFound extends OwnerShipAdditionError {}
    ;
}
