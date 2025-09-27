package backend.bookSharing.repository.entities.request;

import backend.bookSharing.repository.entities.owned.OwnedId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToOne;

@Embeddable
public class RequestId {

    private OwnedId requested;

    private Integer requester_user_id;

}
