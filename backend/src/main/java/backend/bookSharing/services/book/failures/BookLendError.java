package backend.bookSharing.services.book.failures;

public abstract sealed class BookLendError extends Exception permits BookLendError.UserAuthenticationInvalid, BookLendError.RequestNotFound, BookLendError.AlreadyLent{

    private BookLendError(){}

    public static final class UserAuthenticationInvalid extends BookLendError{}

    public static final class RequestNotFound extends  BookLendError{}

    /**
     * A book can only be lent if it is not being currently lent to another
     */
    public static final class AlreadyLent extends BookLendError{}
}
