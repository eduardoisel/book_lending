package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.OwnedId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnedRepository extends JpaRepository<Owned, OwnedId> {

    Page<Owned> findByBookId(Integer bookId, Pageable pageable);

    Page<Owned> findByUserId(Integer userId, Pageable pageable);
}
