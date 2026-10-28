package backend.bookSharing.http.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;

/**
 * @param isbn book identifier, either 10 or 13 numbers
 * @param receiverEmail unique identifier of book's requester
 * */
public record LendCreation(
        @NotNull @ISBN(type = ISBN.Type.ANY) String isbn, @NotBlank String receiverEmail) {}
