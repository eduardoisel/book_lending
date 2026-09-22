package backend.bookSharing.data;

import static backend.bookSharing.repository.entities.Book.Language.English;

import backend.bookSharing.repository.entities.Book;

public class ApiBooks {
    public static Book[] books = {
        new Book("1234567890", "1234567890321", "Test book", English),
    };
}
