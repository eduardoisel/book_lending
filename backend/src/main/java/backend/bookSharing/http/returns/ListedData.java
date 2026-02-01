package backend.bookSharing.http.returns;

/**
 * Generic data class for a list of possibly incomplete data.
 */
public record ListedData(Object[] data, Boolean hasNextPage, Boolean hasPreviousPage) {

}
