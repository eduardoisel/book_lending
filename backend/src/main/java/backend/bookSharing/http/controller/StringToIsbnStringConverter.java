package backend.bookSharing.http.controller;

import backend.bookSharing.http.data.IsbnString;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Convert basic string to simple Record class that holds the string (and has annotations)
 * <p>
 * Spring knows how to do this basic translation by itself, but putting it here anyways.
 */
@Component
public class StringToIsbnStringConverter implements Converter<String, IsbnString> {
    @Override
    public IsbnString convert(@NonNull String source) {
        return new IsbnString(source);
    }
}
