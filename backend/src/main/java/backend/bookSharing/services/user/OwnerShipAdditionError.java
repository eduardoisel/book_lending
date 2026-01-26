package backend.bookSharing.services.user;

public class OwnerShipAdditionError {

    private OwnerShipAdditionError(){}

    public static final class UserAuthenticationInvalid extends OwnerShipAdditionError {};

    public static final class AlreadyMarkedAsOwned extends OwnerShipAdditionError{};

    public static final class BookNotFound extends OwnerShipAdditionError{};

}
