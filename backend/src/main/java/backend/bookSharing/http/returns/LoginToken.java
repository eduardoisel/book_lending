package backend.bookSharing.http.returns;

import jakarta.validation.constraints.NotNull;

/**
 * Information necessary to authenticate
 * @param token the bearer token
 */
public record LoginToken(@NotNull String token) {}
