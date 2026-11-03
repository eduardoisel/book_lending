package backend.bookSharing.http.springdoc;

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
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    /*
    Todo add bearer format, possibly change to use JWT
    https://spec.openapis.org/oas/latest#security-scheme-object
    Link above
     */
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
                                        "bearer",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .in(SecurityScheme.In.HEADER)
                                                .name("Authorization"))
                                // explicit header schema to reference if desired instead of
                                // security Schema
                                .addHeaders(
                                        "Authentication",
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
