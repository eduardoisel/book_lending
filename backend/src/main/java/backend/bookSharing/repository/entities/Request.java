package backend.bookSharing.repository.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Request {

    @EmbeddedId
    private RequestId requestId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("requested")
    @JoinColumns({@JoinColumn(name = "requested_user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "requested_book_id", referencedColumnName = "book_id")})
    private Owned owned;

    @Column(insertable = false)
    private Timestamp date;

    private Integer duration;

    /**
     * Constructor sets all fields, including {@link ManyToOne} relationship
     * @param owned requested book
     * @param requesterId id of user requesting book
     * @param duration duration in days of request
     */
    public Request(Owned owned, Integer requesterId, Integer duration){
        this.requestId = new RequestId(owned.getId(), requesterId);
        this.duration = duration;
        this.owned = owned;
    }

    @Override
    public String toString() {
        return String.format("Request[id='%s', date='%s', duration='%d']", requestId, date, duration);
    }

}
