package backend.bookSharing.repository.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class RequestId {

    private OwnedId requested;

    private Integer requester_user_id;

}
