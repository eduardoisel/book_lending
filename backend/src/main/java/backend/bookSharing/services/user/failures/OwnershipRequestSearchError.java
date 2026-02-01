package backend.bookSharing.services.user.failures;

public abstract sealed class OwnershipRequestSearchError extends Exception permits OwnershipRequestSearchError.OwnershipDoesNotExist {

    private OwnershipRequestSearchError(){}

    public static final class OwnershipDoesNotExist extends OwnershipRequestSearchError{}

}
