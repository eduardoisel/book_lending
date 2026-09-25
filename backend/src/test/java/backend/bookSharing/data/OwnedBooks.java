package backend.bookSharing.data;

import backend.bookSharing.TestData;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.User;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Implementation note: there are complications with inserts in hibernate, due to tables with foreign keys necessitating
 * those keys to already have a non-null value, but those values are only to be found on runtime in this environment
 * <p>
 * Hibernate insert list {@link JpaRepository#saveAllAndFlush(Iterable)} changes the same iterable
 * list, and does not have any way to change its order.
 * As long as the only data this class handles is the manual test data, and those are not altered neither in order nor
 * content, one can save only the position of the elements on the created data list.
 * <p>
 * Current implementation does not assume that, saving more data and performing checks to see if they are related.
 */
public class OwnedBooks {
    /**
     * None contain id of owned, user or book
     */
    public static final Owned[] ownedBooks = {
        new Owned(TestData.users.getFirst(), DatabaseBooks.books[0]),
        new Owned(TestData.users.getFirst(), DatabaseBooks.books[1]),
        new Owned(TestData.users.getFirst(), DatabaseBooks.books[2]),
        new Owned(TestData.users.get(1), DatabaseBooks.books[0]),
    };

    /**
     * Assumes user's username is unique and a book isbn is unique
     *
     * @param users every user
     * @param books every book on database
     * @return
     */
    public static List<Owned> createOwnedListWithForeignIds(List<User> users, List<Book> books) {
        LinkedList<Owned> ownedList = new LinkedList<>();
        Arrays.stream(ownedBooks)
                .forEach(
                        owned -> {
                            User foundUser = getUserWithId(users, owned);
                            Book foundBook = getBookWithId(books, owned);

                            ownedList.add(new Owned(foundUser, foundBook));
                        });

        return ownedList;
    }

    private static Book getBookWithId(List<Book> idBooks, Owned owned) {

        for (Book book : idBooks) {
            if (book.getIsbnTen() != null
                    && owned.getBook().getIsbnTen() != null
                    && book.getIsbnTen().equals(owned.getBook().getIsbnTen())) {
                return book;
            }
            if (book.getIsbnThirteen() != null
                    && owned.getBook().getIsbnThirteen() != null
                    && book.getIsbnThirteen().equals(owned.getBook().getIsbnThirteen())) {
                return book;
            }
        }

        String isbn =
                (owned.getBook().getIsbnTen() != null)
                        ? owned.getBook().getIsbnTen()
                        : owned.getBook().getIsbnThirteen();
        throw new IllegalArgumentException("Book with id" + isbn + "not found");
    }

    private static User getUserWithId(List<User> idUsers, Owned owned) {
        for (User user : idUsers) {
            if (user.getUsername().equals(owned.getUser().getUsername())) {
                return user;
            }
        }
        throw new IllegalArgumentException(
                "User with id" + owned.getUser().getUsername() + "not found");
    }
}
