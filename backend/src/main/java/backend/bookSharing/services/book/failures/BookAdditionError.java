package backend.bookSharing.services.book.failures;

public abstract sealed class BookAdditionError extends Exception permits BookAdditionError.Isbn10InUse, BookAdditionError.Isbn13InUse {

    private BookAdditionError(){}

    public static final class Isbn10InUse extends BookAdditionError{};

    public static final class Isbn13InUse extends BookAdditionError{};
}
