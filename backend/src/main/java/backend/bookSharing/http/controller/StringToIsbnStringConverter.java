package backend.bookSharing.http.controller;

import backend.bookSharing.http.data.IsbnString;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;

/**
 * Convert basic string to simple Record class that holds the string (and has annotations)
 * <p>
 * Spring knows how to do this basic translation by itself, adding this will make springDoc translate the path parameter
 * as a simple string, not a schema. This seems to remove any description and validation restrictions placed on the
 * IsbnString
 */
// @Component
public class StringToIsbnStringConverter implements Converter<String, IsbnString> {
    @Override
    public IsbnString convert(@NonNull String source) {
        return new IsbnString(source);
    }
}
