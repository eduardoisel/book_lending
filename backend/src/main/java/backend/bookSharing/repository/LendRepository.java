package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.Lend;
import backend.bookSharing.repository.entities.OwnedId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LendRepository extends JpaRepository<Lend, OwnedId> {
}
