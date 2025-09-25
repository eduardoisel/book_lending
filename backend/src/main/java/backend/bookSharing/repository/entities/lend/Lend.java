package backend.bookSharing.repository.entities.lend;

import backend.bookSharing.repository.entities.owned.Owned;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.sql.Timestamp;

public class Lend {
    @EmbeddedId
    private LendId lendId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("requested")
    private Owned owned;

    private Timestamp date;

    private Integer duration;

}
