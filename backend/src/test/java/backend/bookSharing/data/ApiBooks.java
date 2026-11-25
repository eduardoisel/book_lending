package backend.bookSharing.data;

import static backend.bookSharing.repository.entities.Book.Language.English;

public class ApiBooks {
    public static TestBook[] books = {
        new TestBook("0671876813", "9780671876814", "Flag in Exile", English),
        new TestBook(null, "9781800649392", "Learning statistics with Jamovi", English)
    };
}
