package backend.bookSharing.repository.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class LendId {

    private OwnedId requested;

    //@OneToOne
    private Integer requester_user_id;

}
