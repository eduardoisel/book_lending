package backend.bookSharing.services.book.failures;

public abstract sealed class BookLendError extends Exception permits BookLendError.RequestNotFound, BookLendError.AlreadyLent{

    private BookLendError(){}

    public static final class RequestNotFound extends  BookLendError{}

    /**
     * A book can only be lent if it is not being currently lent to another
     *
     * Note: this is more restrictive than database model, which allows for multiple lends of the same book.
     */
    public static final class AlreadyLent extends BookLendError{}
}
