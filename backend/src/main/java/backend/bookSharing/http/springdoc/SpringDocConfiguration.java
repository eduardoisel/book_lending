package backend.bookSharing.http.springdoc;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import org.hibernate.validator.constraints.ISBN;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    /**
     * Does not apply the security scheme to any endpoint, only describes it. Using {@link SecurityRequirement} where
     * necessary to indicate it
     */
    @Bean
    OpenAPI openApiConfiguration() {
        return new OpenAPI()
                .components(
                        new Components()
                                .addHeaders(
                                        "Authentication",
                                        // does not reflect on automatic typescript tool (security
                                        // schema is ignored)
                                        // for it to be indicated you need more configuration
                                        // elsewhere
                                        new Header()
                                                .required(true)
                                                .description("Bearer token required")
                                                .schema(new StringSchema()))
                                .addSecuritySchemes(
                                        "bearer",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .in(SecurityScheme.In.HEADER)
                                                .name("Authorization")))
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
    //        };
    //    }

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
                }
            }
            return property;
        };
    }
}
