package backend.bookSharing.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import backend.bookSharing.TestData;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.User;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OwnedBooksTest extends DatabaseTest {

    private final OwnedRepository ownedRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public OwnedBooksTest(
            OwnedRepository ownedRepository,
            BookRepository bookRepository,
            UserRepository userRepository) {
        this.ownedRepository = ownedRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    /**
     * Will be changed by hibernate to have the generated ID, and therefore can be used to inserts that depend on user
     */
    private final List<User> insertedUsers =
            TestData.users.stream().map(TestData::duplicate).toList();

    /**
     * Will be changed by hibernate to have the generated ID, and therefore can be used to inserts that depend on book
     */
    private final List<Book> insertedBooks =
            Arrays.stream(TestData.databaseBooks).map(TestData::duplicate).toList();

    @BeforeEach
    public void insertData() {

        bookRepository.saveAllAndFlush(insertedBooks);

        userRepository.saveAllAndFlush(insertedUsers);
    }

    @Test
    public void createAndSearchTest() {
        Owned savedOwnership = new Owned(insertedUsers.getFirst(), insertedBooks.getFirst());

        assertEquals(0, ownedRepository.count());
        ownedRepository.saveAndFlush(savedOwnership);
        assertEquals(1, ownedRepository.count());

        Owned foundOwnership = ownedRepository.findAll().getFirst();

        assertEquals(savedOwnership.getBook().getId(), foundOwnership.getBook().getId());
        assertEquals(savedOwnership.getUser().getId(), foundOwnership.getUser().getId());
    }

    @Test
    public void deletionTest() {
        assertEquals(0, ownedRepository.count());

        Owned temporaryInsert = new Owned(insertedUsers.getFirst(), insertedBooks.getFirst());

        ownedRepository.save(temporaryInsert);
        ownedRepository.delete(temporaryInsert);

        assertEquals(0, ownedRepository.count());
    }
}
