package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.OwnedId;
import backend.bookSharing.repository.entities.Request;
import backend.bookSharing.repository.entities.RequestId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, RequestId> {

    Page<Request> findByOwnedId(OwnedId id, Pageable pageable);

}
