package backend.bookSharing.http.springdoc;

import backend.bookSharing.http.data.IsbnString;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.util.Iterator;
import org.springframework.stereotype.Component;

/**
 * Replaces {@link StringSchema} with a basic string
 */
@Component
public class IsbnStringConverter implements ModelConverter {
    @Override
    public Schema<?> resolve(
            AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {

        if (type.getType() instanceof Class
                && ((Class<?>) type.getType()).getName().equals(IsbnString.class.getName())) {
            Schema<String> schema = new StringSchema();
            schema.setPattern("^([0-9]{9}|[0-9]{12})([0-9x-xX-X]{1}$)");
            schema.setMinLength(10);
            schema.setMaxLength(13);
            schema.description(
                    "ISBN string, either ISBN 10 or 13. Should also respect ISBN checksum");
            schema.setFormat("ISBN");
            return schema;
        }

        return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
    }
}
