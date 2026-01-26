package backend.bookSharing.services.book.failures;

public sealed class BookAdditionError extends Exception{

    public static final class Isbn10InUse extends BookAdditionError{};

    public static final class Isbn13InUse extends BookAdditionError{};
}
