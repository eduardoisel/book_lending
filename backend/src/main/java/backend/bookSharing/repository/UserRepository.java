package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.Region;
import backend.bookSharing.repository.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;


public interface UserRepository extends JpaRepository<User, Integer> {

    //Streamable<User> findByFirstnameContaining(String firstname);
    //List<User> findByRegion(Region region);
}
