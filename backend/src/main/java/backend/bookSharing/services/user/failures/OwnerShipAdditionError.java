package backend.bookSharing.services.user.failures;

public class OwnerShipAdditionError extends  Exception{

    private OwnerShipAdditionError(){}

    public static final class UserAuthenticationInvalid extends OwnerShipAdditionError {};

    public static final class AlreadyMarkedAsOwned extends OwnerShipAdditionError{};

    public static final class BookNotFound extends OwnerShipAdditionError{};

}
