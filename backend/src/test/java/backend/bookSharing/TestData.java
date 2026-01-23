package backend.bookSharing;

import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Region;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.services.PasswordValidation;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestData {

    static <T> T[] concatWithCollection(T[] array1, T[] array2) {
        List<T> resultList = new ArrayList<>(array1.length + array2.length);
        Collections.addAll(resultList, array1);
        Collections.addAll(resultList, array2);

        @SuppressWarnings("unchecked")
        //the type cast is safe as the array1 has the type T[]
        T[] resultArray = (T[]) Array.newInstance(array1.getClass().getComponentType(), 0);
        return resultList.toArray(resultArray);
    }


    public static Book[] databaseBooks = {
            new Book("0345296052", "9780345296054", "The Fellowship of the Ring", Book.Language.English), //does not have language on openLibrary API, but only on json? https://openlibrary.org/works/OL27513W/The_Fellowship_of_the_Ring?edition=key%3A/books/OL24373119M
            new Book("0553573403", null, "A Game of Thrones", Book.Language.English),
            new Book(null, "9789896419936", "A Muralha", Book.Language.Portuguese), //had the book
            new Book("0910050686", null, "Museum masters", Book.Language.English),
            new Book(null, "9788702347036", "Eventyret om ringen", Book.Language.Danish),
            new Book(null, "9780316312486", "One Dark Window", Book.Language.English),
    };

    public static Book[] booksExclusiveFromApi = {
            new Book("1234567890", "1234567890321", "Test book", Book.Language.English),
    };

    public static Book[] allBooks = concatWithCollection(databaseBooks, booksExclusiveFromApi);

    public static Region[] regions = {
            new Region("Portugal"),
            new Region("England"),
            new Region("USA"),
    };

    private static final PasswordValidation passwordValidation = new PasswordValidation();

    public record ClearPasswordUsers(Region region, String email, String clearPassword) {

        public User toUser() {
                return new User(this.region, this.email, passwordValidation.passwordEncoding(this.clearPassword));
            }

        }

    public static ClearPasswordUsers[] clearPasswordUsers = {
            new ClearPasswordUsers(regions[0], "portugal@gmail.com", "password1"),
            new ClearPasswordUsers(regions[1], "england@gmail.com", "password2"),
            new ClearPasswordUsers(regions[2], "us@gmail.com", "password3"),
    };

    public static List<User> users = Arrays.stream(clearPasswordUsers)
            .map(ClearPasswordUsers::toUser).toList();

}
