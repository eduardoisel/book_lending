package backend.bookSharing.services.user.failures;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract sealed class OwnershipRequestSearchError extends ServiceException
        permits OwnershipRequestSearchError.OwnershipDoesNotExist {

    private OwnershipRequestSearchError() {}

    /**
     * Book in question is not owned by user in question
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class OwnershipDoesNotExist extends OwnershipRequestSearchError {}
}
