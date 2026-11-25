package backend.bookSharing.http.data;

import jakarta.validation.constraints.NotNull;

/**
 *
 * @param id unique identifier for the user
 * @param email user's email/username
 * @param x user's x coordinate
 * @param y user's y coordinate
 */
public record UserDto(int id, @NotNull String email, double x, double y) {
    public UserDto(backend.bookSharing.repository.entities.User user) {
        this(user.getId(), user.getEmail(), user.getLocation().getX(), user.getLocation().getY());
    }
}
