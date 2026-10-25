package backend.bookSharing.http.returns;

import java.util.List;

/**
 * Generic data class for a list of possibly incomplete data.
 */
public record ListedData<T>(List<T> data, Boolean hasNextPage, Boolean hasPreviousPage) {}
