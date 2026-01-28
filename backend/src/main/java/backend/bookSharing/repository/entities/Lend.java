package backend.bookSharing.repository.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/*
 Consider changing info to be on Owned relation
 */
@Setter
@Getter
@Entity
public class Lend {

    @EmbeddedId
    private LendId lendId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId("lent")
    @JoinColumns({@JoinColumn(nullable = false, name = "requested_user_id", referencedColumnName = "user_id"),
            @JoinColumn(nullable = false, name = "requested_book_id", referencedColumnName = "book_id")}) //without it uses field name for part of joinColumn name
    private Owned lent;

    @Column(insertable = false)
    private Timestamp date;

    private Timestamp return_limit;

    public Lend(){}

    /**
     * Constructor sets all fields, including {@link OneToOne} relationship
     * @param request
     */
    public Lend(Request request){
        this.lendId = new LendId(request.getRequestId().getRequested(), request.getRequestId().getRequester_user_id());
        this.return_limit = Timestamp.from(Instant.now().plus(Duration.ofDays(request.getDuration())));
        this.lent = request.getOwned();
    }

    public Lend(LendId id, Integer duration){
        this.lendId = id;
        this.return_limit = Timestamp.from(Instant.now().plus(Duration.ofDays(duration)));
    }

    @Override
    public String toString() {
        return String.format("Lend[id='%s', date='%s', return_limit='%s']", lendId, date, return_limit.toString());
    }

}
