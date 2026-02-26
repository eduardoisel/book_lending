package backend.bookSharing.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
TODO see this mess

Table Lend needs to use its own class id, and it cannot be the exact same in content as OwnedId. I do not know why,
but if i do not do this an exception will be thrown for some reason or another. If i have the 2 ids with the same contents
i had exception thrown because hibernate could not convert OwnedId to LendId. Do not know why it wanted to do that.
When using OwnedId for Lend i could not insert Lend with exception saying Id could not be null, even though i the id
was definitely being let as null
 */
@Getter
@Setter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class LendId{

    //public OwnedId lent;

    @Column(name = "user_id")
    public Integer lentUserId;

    @Column(name = "book_id")
    public Integer lentBookId;

    @Column(nullable = false, name = "requester_user_id")
    private Integer requesterId;


    public LendId(OwnedId ownedId, Integer requesterId) {
        this.lentBookId = ownedId.getBookId();
        this.lentUserId = ownedId.getUserId();
        this.requesterId = requesterId;
    }


}
