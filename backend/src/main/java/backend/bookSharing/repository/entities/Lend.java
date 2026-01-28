package backend.bookSharing.repository.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.Getter;

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

    private Timestamp date;

    private Timestamp return_limit;

    public Lend(){}

    public Lend(Request request){
        this.lendId = new LendId(request.getRequestId().getRequested(), request.getRequestId().getRequester_user_id());
        this.date = request.getDate();
        this.return_limit = Timestamp.from(Instant.now());
        this.lent = request.getOwned();
    }

    public Lend(LendId id, Timestamp date, Timestamp return_limit){
        this.lendId = id;
        this.date = date;
        this.return_limit = return_limit;
    }

    @Override
    public String toString() {
        return String.format("Lend[id='%s', date='%s', return_limit='%s']", lendId, date, return_limit.toString());
    }

}
