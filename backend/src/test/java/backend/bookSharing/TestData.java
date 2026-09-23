package backend.bookSharing;

import backend.bookSharing.data.ApiBooks;
import backend.bookSharing.data.ClearPasswordUser;
import backend.bookSharing.data.DatabaseBooks;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.User;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * Contains base information for testing, as a way to avoid creating information for each unit test, since rollback
 * happens after each ends
 * <p>
 * Warning: Inserting entity will lead to automatically generated id being placed into memory. This leads to exception
 * when attempting to insert the exact same java object again. Solved for now with duplicates
 */
public class TestData {

    static <T> T[] concatWithCollection(T[] array1, T[] array2) {
        List<T> resultList = new ArrayList<>(array1.length + array2.length);
        Collections.addAll(resultList, array1);
        Collections.addAll(resultList, array2);

        @SuppressWarnings("unchecked")
        // the type cast is safe as the array1 has the type T[]
        T[] resultArray = (T[]) Array.newInstance(array1.getClass().getComponentType(), 0);
        return resultList.toArray(resultArray);
    }

    public static final Book[] databaseBooks = DatabaseBooks.books;

    public static Book[] booksExclusiveFromApi = ApiBooks.books;

    public static Book[] allBooks = concatWithCollection(databaseBooks, booksExclusiveFromApi);

    public static Boolean isIsbn10Unique(@NonNull String isbn10) {

        for (Book book : allBooks) {
            if (isbn10.equals(book.getIsbnTen())) {
                return false;
            }
        }
        return true;
    }

    public static Boolean isIsbn13Unique(@NonNull String isbn13) {

        for (Book book : allBooks) {
            if (isbn13.equals(book.getIsbnThirteen())) {
                return false;
            }
        }
        return true;
    }

    //    public record ClearToken(String clearToken, User user) {
    //
    //        public Token toToken(TokenValidation tokenValidation) {
    //            return new
    // Token(tokenValidation.createTokenValidationInformation(this.clearToken), user);
    //        }
    //
    //    }

    private static final GeometryFactory geometryFactory =
            new GeometryFactory(new PrecisionModel(), 4326);

    public static Coordinate[] coordinates = {
            new Coordinate(2.1d, 2.1d), new Coordinate(2.1d, 2.3d), new Coordinate(-20d, 70d),
    };

    public static List<Point> points =
            Arrays.stream(coordinates).map(geometryFactory::createPoint).toList();

    public static ClearPasswordUser[] clearPasswordUsers = {
            new ClearPasswordUser(points.getFirst(), "portugal@gmail.com", "password1"),
            new ClearPasswordUser(points.get(1), "england@gmail.com", "password2"),
            new ClearPasswordUser(points.get(2), "us@gmail.com", "password3"),
    };

    public static Boolean isEmailUnique(@NonNull String email) {

        for (ClearPasswordUser user : clearPasswordUsers) {
            if (email.equals(user.email())) {
                return false;
            }
        }
        return true;
    }

    public static List<User> users =
            Arrays.stream(clearPasswordUsers).map(ClearPasswordUser::toUser).toList();

    /**
     * @param book book to duplicate
     * @return Instance with auto generated id NOT set
     */
    public static Book duplicate(Book book) {
        return new Book(
                book.getIsbnTen(), book.getIsbnThirteen(), book.getTitle(), book.getLanguage());
    }

    /**
     * @param user book to duplicate
     * @return Instance with auto generated id NOT set
     */
    public static User duplicate(User user) {
        return new User(user.getLocation(), user.getEmail(), user.getHash(), user.getSalt());
    }
}
