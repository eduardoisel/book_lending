package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.Region;
import backend.bookSharing.repository.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    //List<User> findByRegion(Region region);
}
