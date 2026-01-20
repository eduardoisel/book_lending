package backend.bookSharing.services.book.api;

import backend.bookSharing.repository.entities.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Component;

@Component
/**
 * Chosen this API from https://www.vinzius.com/post/free-and-paid-api-isbn/
 * This is the only one that seems completely free World cat, the biggest one, had a free xml option for non organizations,
 * but it seems to have been removed. If this idea actually is used consider trying to change the API
 */
public class OpenLibraryApi implements BookApi {

    //allows automatic following of redirects. Needed for the specific url used
    private final HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();

    /*
      * used https://en.wikipedia.org/wiki/List_of_ISO_639_language_codes for languages string
      * as indicated in https://openlibrary.org/dev/docs/api/search to be used the ISO norm above,
      * and https://openlibrary.org/search.json?q=the+lord+of+the+rings shows them being used
     */

    /**
     * @param lang three character string from ISO_639
     * @return Enum representing Language
     */
    private final Book.Language isoIdToLanguage(String lang){
        return switch (lang) {
            case "eng" -> Book.Language.English;
            case "por" -> Book.Language.Portuguese;
            case "spa" -> Book.Language.Spanish;
            case "rus" -> Book.Language.Russian;
            case "jpn" -> Book.Language.Japanese;
            case "ger" -> Book.Language.German;
            case "heb" -> Book.Language.Hebrew;
            case "fra", "fre" -> Book.Language.French;
            case "ita" -> Book.Language.Italian;
            case "hun" -> Book.Language.Hungarian;
            case "dan" -> Book.Language.Danish;
            default ->
                    throw new RuntimeException("Language code %s from external API open library not supported".formatted(lang));
        };
    }

    public final Book getBook(String isbn) {
        //https://openlibrary.org/dev/docs/api/books
        String url = "https://openlibrary.org/isbn/%s.json".formatted(isbn);

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .build();

        String serialized = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404){
                return null;
            }

            serialized = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            throw new RuntimeException(e); //circumvent throws being added to method signature. todo search for better way
        }

        Gson gson = new Gson();

        // https://github.com/google/gson/blob/main/UserGuide.md
        TypeToken<Map<String, Object>> mapType = new TypeToken<Map<String, Object>>() {};
        Map<String, Object> stringMap = gson.fromJson(serialized, mapType);

        //format is /languages/[3 letter code]
        String[] language = ((ArrayList<Map<String, String>>) stringMap.get("languages"))
                .getFirst().get("key")
                .split("/");

        String title = (String) stringMap.get("title");

        ArrayList<String> isbn10 = ((ArrayList<String>) stringMap.get("isbn_10"));
        String isbn_10 = null;

        if (isbn10 != null){
            isbn_10 = isbn10.getFirst();
        }

        ArrayList<String> isbn13 = ((ArrayList<String>) stringMap.get("isbn_13"));
        String isbn_13 = null;

        if (isbn13 != null){
            isbn_13 = isbn13.getFirst();
        }

        return new Book(isbn_10, isbn_13, title, isoIdToLanguage(language[language.length - 1]));

    }


}
