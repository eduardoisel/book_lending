package backend.bookSharing.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
public class OwnedId {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "book_id")
    private Integer bookId;

    public OwnedId(Integer user_id, Integer book_id){
        this.userId = user_id;
        this.bookId = book_id;
    }

    @Override
    public String toString(){
        return String.format("OwnedId[userId='%d', bookId='%d']", userId, bookId);
    }

}
