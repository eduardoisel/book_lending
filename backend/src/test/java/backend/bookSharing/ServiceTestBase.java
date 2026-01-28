package backend.bookSharing;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.RegionRepository;
import backend.bookSharing.repository.TokenRepository;
import backend.bookSharing.repository.UserRepository;
import backend.bookSharing.repository.entities.Token;
import backend.bookSharing.services.user.services.TokenValidation;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import org.springframework.mock.env.*;

/*
 Cannot inherit from Database with data
 */
@SpringBootTest
@Testcontainers
@TestPropertySource(
        properties = {
                "spring.jpa.hibernate.ddl-auto=create-drop",
//                "spring.main.allow-bean-definition-overriding=true"
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

    @Autowired
    private TokenValidation tokenValidation;



    @BeforeEach
    public void insertData(){

        bookRepository.saveAllAndFlush(Arrays.stream(TestData.databaseBooks).toList());

        regionRepository.saveAll(Arrays.stream(TestData.regions).toList());

        userRepository.saveAllAndFlush(TestData.users);

        tokenRepository.saveAllAndFlush(TestData.tokens);

        //userRepository.

    }

}
