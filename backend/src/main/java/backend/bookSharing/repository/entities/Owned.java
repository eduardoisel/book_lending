package backend.bookSharing.repository.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
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

    @JsonIgnore
    @OneToMany(mappedBy = "owned") //note: mapped by string value is from owned class Request field name
    private Set<Request> requests;

    @JsonIgnore
    @OneToOne(mappedBy = "lent") //note: mapped by string value is from owned class Lend field name
    private Lend lend;

    /**
     * Constructor sets id and {@link ManyToOne} relationships
     * @param user owner of book
     * @param book is the book in question
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
