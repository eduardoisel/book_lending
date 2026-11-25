package backend.bookSharing.http.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * todo constraints
 * @param owner
 * @param book
 */
public record OwnedDto(@NotNull @Valid UserDto owner, @NotNull @Valid BookDto book) {
    public OwnedDto(backend.bookSharing.repository.entities.Owned owned) {
        this(new UserDto(owned.getUser()), new BookDto(owned.getBook()));
    }
}
