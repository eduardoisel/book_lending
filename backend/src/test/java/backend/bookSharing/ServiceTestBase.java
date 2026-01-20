package backend.bookSharing;

import backend.bookSharing.repository.BookRepository;
import backend.bookSharing.repository.UserRepository;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/*
 Cannot inherit from Database with data
 */
@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
abstract public class ServiceTestBase {

    @Container
    @ServiceConnection
    static PostgreSQLContainer container = new PostgreSQLContainer("postgres:18-bookworm");

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void insertData(){

        bookRepository.saveAllAndFlush(Arrays.stream(TestData.databaseBooks).toList());

        //userRepository.saveAll(Arrays.stream(TestData.users).toList());

    }
}
