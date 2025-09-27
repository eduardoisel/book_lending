package backend.bookSharing.repository.entities.lend;

import backend.bookSharing.repository.entities.owned.Owned;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Lend {
    @EmbeddedId
    private LendId lendId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("requested")
    private Owned owned;

    @Temporal(TemporalType.TIMESTAMP) //may be unnecessary?
    private Timestamp date;

    private Integer duration;

}
