package backend.bookSharing.repository;

import backend.bookSharing.PostgresDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
public abstract class DatabaseTest extends PostgresDatabase {}
