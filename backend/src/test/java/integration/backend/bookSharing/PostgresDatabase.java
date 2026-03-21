package backend.bookSharing;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
public class PostgresDatabase {

    @Container
    @ServiceConnection
    static protected PostgreSQLContainer container = new PostgreSQLContainer("postgres");

}
