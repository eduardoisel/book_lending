package backend.bookSharing.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

/**
 * @param hash hash of password and salt
 * @param salt
 */
@Embeddable
public record PasswordValidationInfo(
        @Column(length = 256, nullable = false) String hash,
        @Column(length = saltSize, nullable = false) String salt) {
    @Transient public static final int saltSize = 2;
}
