package backend.bookSharing.http.configuration;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;

@Configuration
public class OpenApiConfiguration {

    /**
     * Adds the ProblemDetail class as a schema in case it is not directly noticed to be used
     */
    @Bean
    public OpenApiCustomizer schemaCustomizer() {
        ResolvedSchema resolvedSchema =
                ModelConverters.getInstance()
                        .resolveAsResolvedSchema(new AnnotatedType(ProblemDetail.class));
        return openApi -> openApi.schema(resolvedSchema.schema.getName(), resolvedSchema.schema);
    }
}
