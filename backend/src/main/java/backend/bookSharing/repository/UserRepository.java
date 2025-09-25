package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.Region;
import backend.bookSharing.repository.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByRegion(Region region);
}
