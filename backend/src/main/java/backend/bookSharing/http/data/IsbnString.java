package backend.bookSharing.http.data;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;

/**
 * @param ISBN ISBN string, either ISBN 10 or 13. Should also respect ISBN checksum
 */
public record IsbnString(
        @NotNull @ISBN(type = org.hibernate.validator.constraints.ISBN.Type.ANY) String ISBN) {}
