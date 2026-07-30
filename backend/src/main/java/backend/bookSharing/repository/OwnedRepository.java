package backend.bookSharing.repository;

import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.OwnedId;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OwnedRepository extends JpaRepository<Owned, OwnedId> {

    @Query(value = """
            SELECT * FROM OWNED o JOIN APP_USER u ON o.user_id = u.id WHERE
                        o.book_id = :book_id AND
                        ST_DWithin(u.location, :point, :distance) = true
            """, nativeQuery = true
            )
    Page<Owned> findNearbyOwners(
            @Param("book_id") Integer bookId,
            @Param("point") Point point,
            @Param("distance") double distance,
            Pageable pageable);

    Page<Owned> findByBookId(Integer bookId, Pageable pageable);

    Page<Owned> findByUserId(Integer userId, Pageable pageable);
}
