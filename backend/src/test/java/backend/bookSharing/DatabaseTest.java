package backend.bookSharing;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
@DataJpaTest
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
abstract public class DatabaseTest{

    @Container
    @ServiceConnection
    static protected PostgreSQLContainer container = new PostgreSQLContainer("postgres:18-bookworm");
}
