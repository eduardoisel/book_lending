package backend.bookSharing.repository.entities.owned;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

@Entity
@Embeddable
public class OwnedId {

    private Integer owned_user_id;

    private Integer owned_book_id;

    public Integer getOwned_user_id() {
        return owned_user_id;
    }

    public Integer getOwnedBook_id() {
        return owned_book_id;
    }
}
