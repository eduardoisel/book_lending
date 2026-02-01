package backend.bookSharing.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class LendId{

    //public OwnedId lent;

    @Column(name = "lent_user_id")
    public Integer lentUserId;

    @Column(name = "lent_book_id")
    public Integer lentBookId;

    public LendId(Integer lentUserId, Integer lentBookId) {
        //this.lent = lent;
        this.lentBookId = lentBookId;
        this.lentUserId = lentUserId;
    }

}
