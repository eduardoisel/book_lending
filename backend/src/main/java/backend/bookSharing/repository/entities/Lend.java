package backend.bookSharing.repository.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Lend {

    @EmbeddedId
    public OwnedId id;

    @Column(nullable = false, name = "requester_user_id")
    private Integer requesterId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId("lent") // from LendId
    @JoinColumns({
            @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(nullable = false, name = "book_id", referencedColumnName = "book_id")}) //without it uses field name for part of joinColumn name
    private Owned lent;

    @Column(insertable = false)
    private Timestamp date;

    @Column(name = "return_limit")
    private Timestamp returnLimit;

    /**
     * Constructor sets all fields, including {@link OneToOne} relationship
     * @param request information of request needed for lend
     */
    public Lend(Request request){
        //this.id = new LendId(request.getRequestId().getRequested().getUserId(), request.getRequestId().getRequested().getBookId());
        this.id = request.getRequestId().getRequested();
        this.lent= request.getOwned();
        this.returnLimit = Timestamp.from(Instant.now().plus(Duration.ofDays(request.getDuration())));
        this.requesterId = request.getRequestId().getUserId();
    }

    @Override
    public String toString() {
        return String.format("Lend[Owned='%s', date='%s', returnLimit='%s', requesterId='%d']", super.toString(), date, returnLimit.toString(), requesterId);
    }

}
