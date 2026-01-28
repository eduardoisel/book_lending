package backend.bookSharing.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.IdClass;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class OwnedId {

    @Column(name = "user_id")
    private Integer owned_user_id;

    @Column(name = "book_id")
    private Integer owned_book_id;

    public OwnedId(){

    }

    public OwnedId(Integer user_id, Integer book_id){
        this.owned_user_id = user_id;
        this.owned_book_id = book_id;
    }

    @Override
    public String toString(){
        return String.format("OwnedId[user_id='%d', book_id='%d']", owned_user_id, owned_book_id);
    }

    public Integer getOwned_user_id() {
        return owned_user_id;
    }

    public Integer getOwnedBook_id() {
        return owned_book_id;
    }
}
