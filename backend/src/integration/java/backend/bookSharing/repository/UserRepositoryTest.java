package backend.bookSharing.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import backend.bookSharing.TestData;
import backend.bookSharing.repository.entities.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryTest extends DatabaseTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void createAndSearchTest() {
        User savedUser = TestData.duplicate(TestData.users.getFirst());

        assertEquals(0, userRepository.count());
        userRepository.saveAndFlush(savedUser);
        assertEquals(1, userRepository.count());

        User foundUser = userRepository.findAll().getFirst();

        assertEquals(savedUser.getEmail(), foundUser.getEmail());
        assertEquals(savedUser.getHash(), foundUser.getHash());
    }

    @Test
    public void deletionTest() {
        assertEquals(0, userRepository.count());

        User temporaryInsert = TestData.duplicate(TestData.users.getFirst());

        userRepository.save(temporaryInsert);
        userRepository.delete(temporaryInsert);

        assertEquals(0, userRepository.count());
    }

    @Test
    public void findByEmail() {
        User inserted = TestData.duplicate(TestData.clearPasswordUsers[0].toUser());

        userRepository.save(inserted);

        Optional<User> searched = userRepository.findByEmail(inserted.getEmail());

        assertTrue(searched.isPresent());

        User found = searched.get();

        assertEquals(inserted.getEmail(), found.getEmail());
        assertEquals(inserted.getHash(), found.getHash());
        assertEquals(inserted.getLocation(), found.getLocation());
    }
}
