package backend.bookSharing.services.book.api;

import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.utils.IsbnUtils;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

/**
 * Chosen this API from <a href="https://www.vinzius.com/post/free-and-paid-api-isbn/">vinzius.com</a>
 * This is the only one that seems completely free World cat, the biggest one, had a free XML option for non organizations,
 * but it seems to have been removed. If this idea actually is used consider trying to change the API
 * @implSpec Spring has already JSON parser {@link JsonMapper}, todo update code to use it, remove gson dependency
 */
@Component
@RequiredArgsConstructor
public class OpenLibraryApi implements BookApi {
    // allows automatic following of redirects. Needed for the specific url used
    private final HttpClient client =
            HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();

    private final JsonMapper mapper = new JsonMapper();

    /**
     * @param key follows {@code "/languages/" + ISO_639} format
     */
    private record LanguageFormat(String key) {}

    /**
     * @param title book title
     * @param isbn_10 if not null has at least one element
     * @param isbn_13 if not null has at least one element
     * @param languages if not null has at least one element
     * @implNote parameters need to keep the name as is. They are used by parser.
     */
    private record ApiBook(
            @NotNull String title,
            @Nullable String[] isbn_10,
            @Nullable String[] isbn_13,
            @Nullable LanguageFormat[] languages) {}

    /**
     * {@inheritDoc}
     * @implNote Despite ISBN 10 having by its nature a corresponding ISBN 13, this api may not recognize it, and only
     * store by one of them. See the search of A Game of Thrones by <a href="https://openlibrary.org/isbn/0553573403.json">ISBN 10</a>
     * and <a href="https://openlibrary.org/isbn/9780553573404.json">ISBN 13</a>. Both will work but only one will be
     * found in its contents. For another example, ISBN 9788702347036 is convertible to ISBN 10 but is only stored as
     * ISBN 13
     */
    @Override
    public final Book getBook(@NonNull String isbn) {
        // https://openlibrary.org/dev/docs/api/books
        String url = "https://openlibrary.org/isbn/%s.json".formatted(isbn);

        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();

        String serialized;
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                return null;
            }

            serialized = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            // circumvent throws being added to method signature. todo search for better way
            throw new RuntimeException(e);
        }

        ApiBook apiBook = mapper.readValue(serialized, ApiBook.class);

        // if not null assumes it has one
        if ((apiBook.isbn_10 == null && apiBook.isbn_13 == null) || apiBook.languages == null) {
            throw new IllegalArgumentException();
        }

        String[] language = apiBook.languages[0].key.split("/");

        if (apiBook.isbn_10 == null) {
            return new Book(
                    IsbnUtils.isIsbn13ConvertibleTo10(apiBook.isbn_13[0])
                            ? IsbnUtils.isbn13To10(apiBook.isbn_13[0])
                            : null,
                    apiBook.isbn_13[0],
                    apiBook.title,
                    Book.Language.isoIdToLanguage(language[language.length - 1]));
        } else if (apiBook.isbn_13 == null) {
            return new Book(
                    apiBook.isbn_10[0],
                    IsbnUtils.isbn10To13(apiBook.isbn_10[0]),
                    apiBook.title,
                    Book.Language.isoIdToLanguage(language[language.length - 1]));
        }

        return new Book(
                apiBook.isbn_10[0],
                apiBook.isbn_13[0],
                apiBook.title,
                Book.Language.isoIdToLanguage(language[language.length - 1]));

        /*
        Gson gson = new Gson();


        // https://github.com/google/gson/blob/main/UserGuide.md
        TypeToken<Map<String, Object>> mapType = new TypeToken<Map<String, Object>>() {};
        Map<String, Object> stringMap = gson.fromJson(serialized, mapType);

        // format is /languages/[3 letter code]
        //as indicated in https://openlibrary.org/dev/docs/api/search
        String[] language =
                ((ArrayList<Map<String, String>>) stringMap.get("languages"))
                        .getFirst()
                        .get("key")
                        .split("/");

        String title = (String) stringMap.get("title");

        ArrayList<String> isbn_10 = ((ArrayList<String>) stringMap.get("isbn_10"));
        String isbn_10 = null;

        if (isbn_10 != null) {
            isbn_10 = isbn_10.getFirst();
        }

        ArrayList<String> isbn_13 = ((ArrayList<String>) stringMap.get("isbn_13"));
        String isbn_13 = null;

        if (isbn_13 != null) {
            isbn_13 = isbn_13.getFirst();
        }

        if (isbn_10 == null && isbn_13 != null && IsbnUtils.isIsbn13ConvertibleTo10(isbn_13)) {
            isbn_10 = IsbnUtils.isbn13To10(isbn_13);
        }

        return new Book(isbn_10, isbn_13, title, Book.Language.isoIdToLanguage(language[language.length - 1]));
        */

    }
}
