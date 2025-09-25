package backend.bookSharing.repository.entities.request;

import backend.bookSharing.repository.entities.owned.Owned;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Request {

    @EmbeddedId
    private RequestId requestId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("requested")
    private Owned owned;

    private Timestamp date;

    private Integer duration;
}
