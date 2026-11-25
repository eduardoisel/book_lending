package backend.bookSharing.http.returns;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Listed data, possibly not complete.
 * @param data This slice of the data
 * @param hasNextPage has more data to retrieve
 */
public record ListedData<T>(
        @NotNull List<T> data,
        @NotNull Boolean hasNextPage,
        @NotNull Boolean hasPreviousPage,
        int totalPages) {

    public ListedData(Page<T> page) {
        this(page.toList(), page.hasNext(), page.hasPrevious(), page.getTotalPages());
    }
}
