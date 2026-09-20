package backend.bookSharing.http.data;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 *
 * @param isbn       book identifier, either 10 or 13 numbers
 * @param ownerEmail unique identifier of book's owner
 * @param timeInDays Duration of request
 */
public record RequestCreation(
        @JsonSetter(nulls = Nulls.FAIL) String isbn,
        @JsonSetter(nulls = Nulls.FAIL) String ownerEmail,
        @JsonSetter(nulls = Nulls.FAIL) Integer timeInDays) {}
