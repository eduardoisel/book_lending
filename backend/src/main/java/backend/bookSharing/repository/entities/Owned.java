package backend.bookSharing.repository.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.Set;

@Entity
public class Owned{

    @EmbeddedId
    private OwnedId id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("owned_user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("owned_book_id")
    private Book book;

    @OneToMany(mappedBy = "owned") //note: mapped by string value is from owned class Request field name
    private Set<Request> requests;

    @OneToOne(mappedBy = "lent") //note: mapped by string value is from owned class Lend field name
    private Lend lend;

    public Owned(){

    }

    public Owned(OwnedId id){
        this.id = id;
    }

    @Override
    public String toString(){
        return String.format("Owned[id='%s']", id.toString());
    }

    public User getUser(){
        return user;
    }

    public Book getBook(){
        return book;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public Lend getLend() {
        return lend;
    }
}
