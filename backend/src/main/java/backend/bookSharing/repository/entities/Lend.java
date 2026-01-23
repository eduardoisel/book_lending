package backend.bookSharing.repository.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
public class Lend {

    @EmbeddedId
    private LendId lendId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId("lent")
    @JoinColumns({@JoinColumn(name = "requested_user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "requested_book_id", referencedColumnName = "book_id")}) //without it uses field name for part of joinColumn name
    private Owned lent;

    private Timestamp date;

    private Timestamp return_limit;

    public Lend(){} //todo check if necessary

    public Lend(Request request){
        this.lendId = new LendId(request.id().getRequested(), request.id().getRequester_user_id());
        this.date = request.getDate();
        this.return_limit = Timestamp.from(Instant.now()); // todo change
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


    public LendId getLendId() {
        return lendId;
    }

    public Timestamp getDate() {
        return date;
    }

    public Timestamp getReturn_limit() {
        return return_limit;
    }
}
