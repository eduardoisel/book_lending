package backend.bookSharing.http.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.sql.Timestamp;

/**
 * Information about requests of specific user's owned book
 * @param requester
 * @param date date request was first made
 * @param requestDuration duration of request in days
 */
public record RequestsOfOwnership(
        @NotNull @Valid UserDto requester,
        @NotNull @Valid Timestamp date,
        @Positive int requestDuration) {
    public RequestsOfOwnership(backend.bookSharing.repository.entities.Request request) {
        this(new UserDto(request.getRequester()), request.getDate(), request.getDuration());
    }
}
