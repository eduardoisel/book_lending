package backend.bookSharing.http.data;

/**
 * Information necessary to authenticate
 * @param token the bearer token
 */
public record LoginToken(String token) {}
