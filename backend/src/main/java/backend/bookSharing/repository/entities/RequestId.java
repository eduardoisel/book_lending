package backend.bookSharing.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class RequestId {

    private OwnedId requested;

    @Column(name = "requester_user_id")
    private Integer userId;

    public RequestId(OwnedId requested, Integer userId){
        this.requested = requested;
        this.userId = userId;
    }

    @Override
    public String toString(){
        return String.format(
                "RequestId[ownedId='%s', requesterUserId='%d']",
                requested.toString(), userId);
    }

}
