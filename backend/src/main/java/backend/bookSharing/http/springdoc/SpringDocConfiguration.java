package backend.bookSharing.http.springdoc;

import backend.bookSharing.http.data.IsbnString;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import org.hibernate.validator.constraints.ISBN;
import org.springdoc.core.converters.ModelConverterRegistrar;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;

@Configuration
public class SpringDocConfiguration {

    /*
    Todo add bearer format, possibly change to use JWT
    https://spec.openapis.org/oas/latest#security-scheme-object
    Link above
     */

    static String BEARER_SECURITY_SCHEME = "bearer";

    /**
     * Does not apply the security scheme to any endpoint, only describes it. Using {@link SecurityRequirement} where
     * necessary to indicate it
     */
    @Bean
    OpenAPI openApiConfiguration() {
        return new OpenAPI()
                .components(
                        new Components()
                                // openApi TypeScript tool ignores security schema
                                // would need to make it explicit parameter for it to be translated
                                .addSecuritySchemes(
                                        BEARER_SECURITY_SCHEME,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .in(SecurityScheme.In.HEADER)
                                                .name("Authorization"))
                                // explicit header schema to reference if desired instead of
                                // security Schema
                                .addHeaders(
                                        HttpHeaders.AUTHORIZATION,
                                        new Header()
                                                .required(true)
                                                .description("Bearer token required")
                                                .schema(new StringSchema())))

                // .addSecurityItem(new SecurityRequirement().addList("bearer"))
                .info(
                        new Info()
                                .title("Book lending")
                                .contact(
                                        new Contact()
                                                .name("Eduardo Tavares")
                                                .email("eduardodinis3@gmail.com")));
    }

    //
    //    public DelegatingMethodParameterCustomizer delegatingMethodParameterCustomizer() {
    //        return new DelegatingMethodParameterCustomizer() {
    //            @Override
    //            public void customize(MethodParameter originalParameter, MethodParameter
    // methodParameter) {
    //
    //            }
    //    }

    /**
     * A string annotated with {@link ISBN} will have restrictions. Does NOT work on {@link IsbnString} passed on path
     * parameter
     */
    @Bean
    PropertyCustomizer isbnPropertyCustomizer() {
        return (property, type) -> {
            if (type.getCtxAnnotations() != null) {
                boolean hasISBN =
                        Arrays.stream(type.getCtxAnnotations())
                                .anyMatch(
                                        annotation ->
                                                annotation.annotationType().equals(ISBN.class));

                if (hasISBN) {
                    property.setFormat("ISBN");
                    property.setPattern("^([0-9]{9}|[0-9]{12})([0-9x-xX-X]{1}$)");
                    property.setMinLength(10);
                    property.setMaxLength(13);
                    // uncomment below to change description
                    // property.description("ISBN string, either ISBN 10 or 13. Should also respect
                    // ISBN checksum");
                }
            }
            return property;
        };
    }

    /**
     * @implNote This does not replace original ModelResolver, and is added as an element in the ModelConverter chain.
     * Given the highest precedence, it is the first bean on the list of the converters (except the original, which is placed last)
     * making it the last on the chain seen {@link ModelConverterRegistrar#ModelConverterRegistrar here} where each element
     * is placed first each time, making it being inserted backwards.
     *
     * @implSpec  Following the {@link ModelConverters#ModelConverters(boolean openapi31) ModelResolver construction},
     * assuming it is openApi31
     *
     * @return Extended class that overrides method, making all primitives required
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ModelResolver customModelResolver() {
        ObjectMapper openapi31Mapper = Json31.mapper();
        ModelResolver replacement =
                new ModelResolver(openapi31Mapper) {
                    @Override
                    protected Schema.RequiredMode resolveRequiredMode(
                            Schema schema, JavaType javaType) {

                        if (javaType.isPrimitive()) {
                            return Schema.RequiredMode.REQUIRED;
                        }

                        return super.resolveRequiredMode(schema);
                    }
                };
        replacement.openapi31(true);

        return replacement;
    }
}
