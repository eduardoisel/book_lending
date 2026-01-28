package backend.bookSharing.repository.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@EqualsAndHashCode
public class Owned{

    @EmbeddedId
    private OwnedId id;

    @Setter
    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("owned_user_id")
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    private User user;

    @Setter
    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("owned_book_id")
    @JoinColumn(nullable = false, name = "book_id", referencedColumnName = "id")
    private Book book;

    @OneToMany(mappedBy = "owned") //note: mapped by string value is from owned class Request field name
    private Set<Request> requests;

    @OneToOne(mappedBy = "lent") //note: mapped by string value is from owned class Lend field name
    private Lend lend;


    public Owned(){

    }

    /**
     * Constructor sets only id, nothing more. Works for searching, but insert also needs {@link ManyToOne} to be set
     * @param id id of entity
     */
    public Owned(OwnedId id){
        this.id = id;
    }

    /**
     * Constructor sets id and {@link ManyToOne} relationships
     * @param user
     * @param book
     */
    public Owned(User user, Book book){
        this.user = user;
        this.book = book;
        this.id = new OwnedId(user.getId(), book.getId());
    }

    @Override
    public String toString(){
        return String.format("Owned[id='%s']", id.toString());
    }

}
