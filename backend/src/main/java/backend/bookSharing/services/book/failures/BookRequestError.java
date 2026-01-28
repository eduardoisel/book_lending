package backend.bookSharing.services.book.failures;

public abstract sealed class BookRequestError extends Exception permits BookRequestError.OwnershipNotFound, BookRequestError.AlreadyRequested, BookRequestError.UserAuthenticationInvalid, BookRequestError.CannotRequestFromSelf{

    private BookRequestError(){}

    public static final class UserAuthenticationInvalid extends BookRequestError{}

    public static final class CannotRequestFromSelf extends BookRequestError{}

    public static final class OwnershipNotFound extends  BookRequestError{}

    /**
     * Only thrown if requested book from that specific person, policy may be changed
     */
    public static final class AlreadyRequested extends BookRequestError{}

}
