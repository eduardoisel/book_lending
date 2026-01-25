package backend.bookSharing.repository;

import backend.bookSharing.DatabaseTest;
import backend.bookSharing.TestData;
import backend.bookSharing.repository.entities.User;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Rollback
public class UserRepositoryTest extends DatabaseTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegionRepository regionRepository;

    @BeforeEach
    public void insertRegions(){
        regionRepository.saveAll(Arrays.stream(TestData.regions).toList());
    }

    @Test
    public void createAndSearchTest(){
        User savedUser = TestData.users.getFirst();

        assertEquals(0, userRepository.count());
        userRepository.save(savedUser);
        assertEquals(1, userRepository.count());

        User foundUser = userRepository.findAll().getFirst();

        assertEquals(savedUser.getEmail(), foundUser.getEmail());

        assertEquals(savedUser.getPassword(), foundUser.getPassword());

    }

    @Test
    public void deletionTest(){
        User temporaryInsert = TestData.users.get(0);

        userRepository.save(temporaryInsert);

        userRepository.delete(temporaryInsert);

        assertEquals(0, userRepository.count());
    }

    @Test
    public void findByEmail(){
        User inserted = TestData.users.get(0);

        userRepository.save(inserted);

        Optional<User> searched = userRepository.findByEmail(inserted.getEmail());

        assertTrue(searched.isPresent());

        User found = searched.get();

        assertEquals(inserted.getEmail(), found.getEmail());
        assertEquals(inserted.getPassword(), found.getPassword());
        assertEquals(inserted.getRegion(), found.getRegion());

    }

}
