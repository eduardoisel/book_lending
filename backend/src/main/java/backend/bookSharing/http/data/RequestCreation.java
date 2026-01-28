package backend.bookSharing.http.data;

import org.springframework.lang.NonNullApi;

/**
 *
 * @param isbn book identifier, either 10 or 13 numbers
 * @param ownerEmail unique identifier of book's owner
 * @param timeInDays Duration of request
 */
public record RequestCreation(String isbn, String ownerEmail, Integer timeInDays) {
}
