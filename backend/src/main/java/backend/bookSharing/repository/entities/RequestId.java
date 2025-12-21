package backend.bookSharing.repository.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;

@Embeddable
public class RequestId {

    private OwnedId requested;

    private Integer requester_user_id;

    public RequestId(){

    }

    public RequestId(OwnedId requested, Integer requester_user_id){
        this.requested = requested;
        this.requester_user_id = requester_user_id;
    }

    @Override
    public String toString(){
        return String.format(
                "RequestId[owner_user_id='%d', book_id='%d', requester_user_id='%d']",
                requested.getOwned_user_id(), requested.getOwnedBook_id(), requester_user_id);
    }


    public Integer getRequester_user_id() {
        return this.requester_user_id;
    }

    public OwnedId getRequested(){
        return this.requested;
    }

}
