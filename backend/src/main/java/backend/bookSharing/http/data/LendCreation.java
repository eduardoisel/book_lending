package backend.bookSharing.http.data;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 * @param isbn book identifier, either 10 or 13 numbers
 * @param receiverEmail unique identifier of book's requester
 * */
public record LendCreation(
        @JsonSetter(nulls = Nulls.FAIL) String isbn,
        @JsonSetter(nulls = Nulls.FAIL) String receiverEmail) {}
