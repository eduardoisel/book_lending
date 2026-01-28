package backend.bookSharing.utils;

/**
 *
 * @param hash hash of password and salt
 * @param salt
 */
public record PasswordValidationInfo(String hash, String salt) {
}
