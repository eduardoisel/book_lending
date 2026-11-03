package backend.bookSharing.data;

import static backend.bookSharing.repository.entities.Book.Language.English;

import backend.bookSharing.repository.entities.Book;

public class ApiBooks {
    public static Book[] books = {
        new Book("0671876813", "9780671876814", "Flag in Exile", English),
        new Book(null, "9781800649392", "Learning statistics with Jamovi", English)
    };
}
