package backend.bookSharing.services;

import backend.bookSharing.MockUsage;
import backend.bookSharing.TestData;
import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.RegionRepository;
import backend.bookSharing.repository.TokenRepository;
import backend.bookSharing.repository.UserRepository;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@Testcontainers
@TestPropertySource(
        properties = {
                "spring.jpa.hibernate.ddl-auto=create-drop",
        })
@Import(MockUsage.class)
abstract public class ServiceTestBase {

    @Container
    @ServiceConnection
    static PostgreSQLContainer container = new PostgreSQLContainer("postgres");

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

//    @Autowired
//    public ServiceTestBase(RegionRepository regionRepository, BookRepository bookRepository, UserRepository userRepository, TokenRepository tokenRepository, TokenValidation tokenValidation) {
//        this.regionRepository = regionRepository;
//        this.bookRepository = bookRepository;
//        this.userRepository = userRepository;
//        this.tokenRepository = tokenRepository;
//        this.tokenValidation = tokenValidation;
//    }

    @BeforeEach
    public void insertData() {

        bookRepository.saveAllAndFlush(Arrays.stream(TestData.databaseBooks).toList());

        regionRepository.saveAll(Arrays.stream(TestData.regions).toList());

        userRepository.saveAllAndFlush(TestData.users);

        tokenRepository.saveAllAndFlush(TestData.tokens);

    }

}
