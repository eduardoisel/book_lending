package backend.bookSharing.services;

import backend.bookSharing.MockUsage;
import backend.bookSharing.PostgresDatabase;
import backend.bookSharing.TestData;
import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.TokenRepository;
import backend.bookSharing.repository.UserRepository;
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

    @Autowired private TokenRepository tokenRepository;

    /**
     * Will be changed by hibernate to have the generated ID, and therefore can be used to inserts that depend on user
     */
    protected List<User> insertedUsers = TestData.users.stream().map(TestData::duplicate).toList();

    @BeforeEach
    public void insertData() {

        bookRepository.saveAllAndFlush(
                Arrays.stream(TestData.databaseBooks).map(TestData::duplicate).toList());

        userRepository.saveAllAndFlush(insertedUsers);
    }
}
