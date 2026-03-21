package backend.bookSharing.repository;

import backend.bookSharing.PostgresDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;


@DataJpaTest
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
abstract public class DatabaseTest extends PostgresDatabase {

}
