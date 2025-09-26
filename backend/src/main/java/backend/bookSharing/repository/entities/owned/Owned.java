package backend.bookSharing.repository.entities.owned;

import backend.bookSharing.repository.entities.book.Book;
import backend.bookSharing.repository.entities.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

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
