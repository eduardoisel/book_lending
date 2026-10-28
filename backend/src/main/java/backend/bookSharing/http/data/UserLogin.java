package backend.bookSharing.http.data;

import jakarta.validation.constraints.NotNull;

public record UserLogin(@NotNull String email, @NotNull String password) {}
