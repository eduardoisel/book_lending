package backend.bookSharing.services.book.failures;

/**
 * Exception thrown when trying to add book information
 */
public abstract sealed class BookAdditionError extends Exception permits BookAdditionError.Isbn10InUse, BookAdditionError.Isbn13InUse, BookAdditionError.BookNotFound {

    private BookAdditionError(){}

    /**
     * Was given an isbn 10 identifier already in database
     */
    public static final class Isbn10InUse extends BookAdditionError{}

    /**
     * Was given an isbn 13 identifier already in database
     */
    public static final class Isbn13InUse extends BookAdditionError{}

    /**
     * Book was not found to exist from given isbn
     */
    public static final class BookNotFound extends BookAdditionError{}

}
