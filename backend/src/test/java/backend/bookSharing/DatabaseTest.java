package backend.bookSharing;

import backend.bookSharing.repository.UserRepositoryTest;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Base class for tests that only use the database
 *
 * Todo / warning: each test can affect the other. change {@link UserRepositoryTest} to use the same user and only the
 * first will pass
 */
@Testcontainers
@DataJpaTest
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
abstract public class DatabaseTest{

    @Container
    @ServiceConnection
    static protected PostgreSQLContainer container = new PostgreSQLContainer("postgres");
}
