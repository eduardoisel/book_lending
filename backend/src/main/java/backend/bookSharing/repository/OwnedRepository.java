package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.OwnedId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnedRepository extends JpaRepository<Owned, OwnedId> {
}
