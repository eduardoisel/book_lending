package backend.bookSharing.services;

import backend.bookSharing.MockUsage;
import backend.bookSharing.PostgresDatabase;
import backend.bookSharing.TestData;
import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.RegionRepository;
import backend.bookSharing.repository.TokenRepository;
import backend.bookSharing.repository.UserRepository;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.jpa.hibernate.ddl-auto=create-drop",
        })
@Import(MockUsage.class)
@Transactional //rollback after each unit test
public class ServiceTestBase extends PostgresDatabase {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @BeforeEach
    public void insertData() {

        bookRepository.saveAllAndFlush(Arrays.stream(TestData.databaseBooks).map(TestData::duplicate).toList());

        regionRepository.saveAll(Arrays.stream(TestData.regions).toList());

        userRepository.saveAllAndFlush(TestData.users);

    }

}
