package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Streamable<User> findByFirstnameContaining(String firstname);

    Optional<User> findByEmail(String email);
}
