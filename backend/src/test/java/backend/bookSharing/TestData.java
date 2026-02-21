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
import lombok.NonNull;

import static backend.bookSharing.repository.entities.Book.Language.English;
import static backend.bookSharing.repository.entities.Book.Language.French;

/*
 Uses PasswordValidation directly (without Autowired)
 */
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
        //the type cast is safe as the array1 has the type T[]
        T[] resultArray = (T[]) Array.newInstance(array1.getClass().getComponentType(), 0);
        return resultList.toArray(resultArray);
    }

    public static final Book[] databaseBooks = {
            new Book("0345296052", "9780345296054", "The Fellowship of the Ring", English), //does not have language on openLibrary API, but only on json? https://openlibrary.org/works/OL27513W/The_Fellowship_of_the_Ring?edition=key%3A/books/OL24373119M
            new Book("0553573403", null, "A Game of Thrones", English),
            new Book(null, "9789896419936", "A Muralha", Book.Language.Portuguese), //had the book
            new Book("0910050686", null, "Museum masters", English),
            new Book(null, "9788702347036", "Eventyret om ringen", Book.Language.Danish),
            new Book(null, "9780316312486", "One Dark Window", English),
            new Book("0810984911", null, "The Ugly Truth", English),
            new Book("0141182474", null, "Nineteen Eighty-Four", English),
            new Book("0517020785", "9780517020784", "The Natural House", English),
            new Book("1535299533", "9781535299534", "War And Peace", English),
            new Book("1444808249", "9781444808247", "The Case Book of Sherlock Holmes", English),
            new Book("140430732X", "9781404307322", "The History of Herodotus", English),//
            new Book("1542878160", "9781542878166", "The Great Gatsby", English),
            new Book("1600965229", "9781600965227", "The Man Who Knew Too Much", English),
            new Book("1440471045", "9781440471049", "Siddhartha", English),
            new Book("0671453521", null, "Wuthering Heights", English),
            new Book("0671481193", "9780671481193", "Vanity Fair", English),
            new Book("0820703931", "9780820703930", "Paradise Lost", English),
            new Book("1582872554", "9781582872551", "The lost princess of Oz", English),
            new Book(null, "9798604654095", "Paradise Lost", English),
            new Book("0820703931", "9780820703930", "Anne of Green Gables", English),
            new Book("1566194326", "9781566194327", "Crime and Punishment (Barnes and Noble Classics)", English),
            new Book("1145233589", "9781145233584", "Le Comte de Monte-Cristo", French),
    };

    public static Book[] booksExclusiveFromApi = {
            new Book("1234567890", "1234567890321", "Test book", English),
    };

    public static Book[] allBooks = concatWithCollection(databaseBooks, booksExclusiveFromApi);

    public static Boolean isIsbn10Unique(@NonNull String isbn10){

        for (Book book : allBooks) {
            if (isbn10.equals(book.getIsbnTen())) {
                return false;
            }
        }
        return true;

    }

    public static Boolean isIsbn13Unique(@NonNull String isbn13){

        for (Book book : allBooks) {
            if (isbn13.equals(book.getIsbnThirteen())) {
                return false;
            }
        }
        return true;

    }

    public static Region[] regions = {
            new Region("Portugal"),
            new Region("England"),
            new Region("USA"),
    };

    private static final PasswordValidation passwordValidation = new PasswordValidation();

    public record ClearPasswordUsers(Region region, String email, String clearPassword) {

        public User toUser() {
            String salt = passwordValidation.getSalt();
            return new User(this.region, this.email, passwordValidation.passwordEncoding(this.clearPassword, salt), salt);
        }

    }

//    public record ClearToken(String clearToken, User user) {
//
//        public Token toToken(TokenValidation tokenValidation) {
//            return new Token(tokenValidation.createTokenValidationInformation(this.clearToken), user);
//        }
//
//    }

    public static ClearPasswordUsers[] clearPasswordUsers = {
            new ClearPasswordUsers(regions[0], "portugal@gmail.com", "password1"),
            new ClearPasswordUsers(regions[1], "england@gmail.com", "password2"),
            new ClearPasswordUsers(regions[2], "us@gmail.com", "password3"),
    };

    public static Boolean isEmailUnique(@NonNull String email){

        for (ClearPasswordUsers user : clearPasswordUsers) {
            if (email.equals(user.email)) {
                return false;
            }
        }
        return true;

    }

    public static List<User> users = Arrays.stream(clearPasswordUsers)
            .map(ClearPasswordUsers::toUser).toList();

    /**
     * @param book book to duplicate
     * @return Instance with auto generated id NOT set
     */
    public static Book duplicate(Book book){
        return new Book(
                book.getIsbnTen(),
                book.getIsbnThirteen(),
                book.getTitle(),
                book.getLanguage()
        );
    }

    /**
     * @param user book to duplicate
     * @return Instance with auto generated id NOT set
     */
    public static User duplicate(User user){
        return new User(
                user.getRegion(),
                user.getEmail(),
                user.getHash(),
                user.getSalt()
        );
    }

}
