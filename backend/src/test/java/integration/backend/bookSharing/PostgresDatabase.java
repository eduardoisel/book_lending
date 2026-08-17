package backend.bookSharing;

import java.time.Duration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class PostgresDatabase {

    @Container
    @ServiceConnection
    static protected PostgreSQLContainer container =
            new PostgreSQLContainer(DockerImageName.parse("postgis/postgis:12-3.0")
                    .asCompatibleSubstituteFor("postgres"))
                    .withReuse(true)
                    .withStartupTimeout(Duration.ofMinutes(2));

}
