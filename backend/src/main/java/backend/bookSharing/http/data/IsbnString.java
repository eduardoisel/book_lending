package backend.bookSharing.http.data;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;

/**
 * In special class to contain validation annotations, helps both spring and code recognize that it has restriction due
 * to it being glued to the class
 * @param ISBN ISBN string, either ISBN 10 or 13. Should also respect ISBN checksum
 */
public record IsbnString(
        @NotNull @ISBN(type = org.hibernate.validator.constraints.ISBN.Type.ANY) String ISBN) {}
