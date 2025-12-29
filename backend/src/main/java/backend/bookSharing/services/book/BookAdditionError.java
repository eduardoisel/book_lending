package backend.bookSharing.services.book;

public sealed class BookAdditionError {

    public static final class Isbn10InUse extends BookAdditionError{};
    public static final class Isbn13InUse extends BookAdditionError{};
}
