package backend.bookSharing.repository.entities.owned;

import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.User;
import jakarta.persistence.*;

@Entity
public class Owned {

    @EmbeddedId
    private OwnedId id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("owned_user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("owned_book_id")
    private Book book;

}
