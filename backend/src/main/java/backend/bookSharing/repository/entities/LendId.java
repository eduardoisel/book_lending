package backend.bookSharing.repository.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import lombok.Getter;

@Getter
@Embeddable
public class LendId {

    private OwnedId lent;

    private Integer requester_user_id;

    public LendId(){} //todo check if necessary

    public LendId(OwnedId lent, Integer requesterUserId) {
        this.lent = lent;
        this.requester_user_id = requesterUserId;
    }

}
