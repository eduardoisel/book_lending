package backend.bookSharing.services.user.failures;

public sealed abstract class OwnerShipAdditionError extends Exception permits OwnerShipAdditionError.AlreadyMarkedAsOwned, OwnerShipAdditionError.BookNotFound{

    private OwnerShipAdditionError(){}

    public static final class AlreadyMarkedAsOwned extends OwnerShipAdditionError{};

    public static final class BookNotFound extends OwnerShipAdditionError{};

}
