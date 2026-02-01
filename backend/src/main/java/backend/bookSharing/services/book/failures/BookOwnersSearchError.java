package backend.bookSharing.services.book.failures;

public abstract sealed class BookOwnersSearchError extends Exception permits BookOwnersSearchError.BookNotFound{

    private BookOwnersSearchError(){}

    /**
     * Thrown when book does not exist on database
     */
    public static final class BookNotFound extends BookOwnersSearchError{}

}
