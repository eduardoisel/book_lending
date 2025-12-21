package backend.bookSharing.repository.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Duration;

@Entity
public class Request {

    @EmbeddedId
    private RequestId requestId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("requested")
    @JoinColumns({@JoinColumn(name = "requested_user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "requested_book_id", referencedColumnName = "book_id")}) //without it uses field name for part of joinColumn name
    private Owned owned;

    private Timestamp date;

    private Integer duration;

    public Request(){} //todo check if necessary

    public Request(RequestId id, Timestamp date, Integer duration){
        this.requestId = id;
        this.date = date;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format("Request[id='%s', date='%s', duration='%d']", requestId, date, duration);
    }

    public Owned getOwned(){
        return this.owned;
    }

    public Timestamp getDate() {
        return date;
    }

    public Integer getDuration() {
        return duration;
    }

    public RequestId id(){
        return requestId;
    }

}
