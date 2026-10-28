package backend.bookSharing.http.returns;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Generic data class for a list of possibly incomplete data.
 */
public record ListedData<T>(
        @NotNull List<T> data, @NotNull Boolean hasNextPage, @NotNull Boolean hasPreviousPage) {}
