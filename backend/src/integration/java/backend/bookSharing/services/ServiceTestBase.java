package backend.bookSharing.services;

import backend.bookSharing.MockUsage;
import backend.bookSharing.PostgresDatabase;
import backend.bookSharing.TestData;
import backend.bookSharing.data.OwnedBooks;
import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.OwnedRepository;
import backend.bookSharing.repository.TokenRepository;
import backend.bookSharing.repository.UserRepository;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.User;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(MockUsage.class)
@Transactional // rollback after each unit test
public class ServiceTestBase extends PostgresDatabase {

    @Autowired private BookRepository bookRepository;

    @Autowired private UserRepository userRepository;

    @Autowired private OwnedRepository ownedRepository;

    @Autowired private TokenRepository tokenRepository;

    /**
     * Will be changed by hibernate to have the generated ID, and therefore can be used to inserts that depend on user
     */
    protected List<User> insertedUsers = TestData.users.stream().map(TestData::duplicate).toList();

    /**
     * Will be changed by hibernate to have the generated ID, and therefore can be used to inserts that depend on book
     */
    protected List<Book> insertedBooks =
            Arrays.stream(TestData.databaseBooks).map(TestData::duplicate).toList();

    protected List<Owned> insertedOwned;

    @BeforeEach
    public void insertData() {

        bookRepository.saveAllAndFlush(insertedBooks);

        userRepository.saveAllAndFlush(insertedUsers);

        insertedOwned = OwnedBooks.createOwnedListWithForeignIds(insertedUsers, insertedBooks);

        ownedRepository.saveAllAndFlush(insertedOwned);
    }
}
