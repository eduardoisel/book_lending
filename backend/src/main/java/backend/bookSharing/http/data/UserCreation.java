package backend.bookSharing.http.data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * data class with information necessary to create user
 *
 * @param email    email to be associated to user, cannot be in use for another account at the same time
 * @param password clear password to use on login
 * @param x        x coordinate of user location
 * @param y        y coordinate of user location
 */
public record UserCreation(
        @NotNull String email,
        @NotNull @Size(min = 8) @Pattern(regexp = ".*[a-z].*", message = "requires lower case char") @Pattern(regexp = ".*[A-Z].*", message = "requires upper case char") @Pattern(regexp = ".*[^a-z0-9A-Z].*", message = "requires special char") String password,
        @Min(-180) @Max(180) double x,
        @Min(-90) @Max(90) double y) {}
