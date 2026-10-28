package backend.bookSharing.http.data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;

/**
 *
 * @param isbn       book identifier, either 10 or 13 numbers
 * @param ownerEmail unique identifier of book's owner
 * @param timeInDays Duration of request
 */
public record RequestCreation(
        @NotNull @ISBN(type = ISBN.Type.ANY) String isbn,
        @NotNull String ownerEmail,
        @NotNull @Min(value = 1, message = "Must be lent for at least one day") Integer timeInDays) {}
