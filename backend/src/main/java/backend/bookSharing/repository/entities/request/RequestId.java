package backend.bookSharing.repository.entities.request;

import backend.bookSharing.repository.entities.owned.OwnedId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.OneToOne;

@Embeddable
public class RequestId {

    @EmbeddedId
    private OwnedId requested;

    @OneToOne
    private Integer requester_user_id;

}
