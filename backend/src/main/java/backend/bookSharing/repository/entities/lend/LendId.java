package backend.bookSharing.repository.entities.lend;

import backend.bookSharing.repository.entities.owned.OwnedId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToOne;

@Embeddable
public class LendId {

    private OwnedId requested;

    //@OneToOne
    private Integer requester_user_id;

}
