package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, String> {
}
