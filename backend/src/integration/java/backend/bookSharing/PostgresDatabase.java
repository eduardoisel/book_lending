package backend.bookSharing;

import java.time.Duration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/*
Using @TestContainers will create a new database for each test class, an issue since a test class with the same spring
testContext will expect the same database
 */
public class PostgresDatabase {

    @Container
    @ServiceConnection
    static protected PostgreSQLContainer container =
            new PostgreSQLContainer(DockerImageName.parse("postgis/postgis:12-3.0")
                    .asCompatibleSubstituteFor("postgres"))
                    .withReuse(true)
                    .withStartupTimeout(Duration.ofMinutes(2));

}
