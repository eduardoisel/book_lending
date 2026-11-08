package backend.bookSharing.http.springdoc;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import org.hibernate.validator.constraints.ISBN;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;

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

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * @param objectMapper parameter required to create base class
     * @return Extended class that overrides method, making all primitives required
     */
    @Bean
    public ModelResolver customModelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper) {
            @Override
            protected Schema.RequiredMode resolveRequiredMode(Schema schema, JavaType javaType) {

                if (javaType.isPrimitive()) {
                    return Schema.RequiredMode.REQUIRED;
                }

                return super.resolveRequiredMode(schema);
            }
        };
    }

    /**
     * Todo would describe what filter does. Not operational in the slightest
     * @return
     */
    // @Bean
    // @ConditionalOnBean(FilterRegistrationBean.class)
    OperationCustomizer operationCustomizer() {
        return new OperationCustomizer() {
            @Override
            public Operation customize(Operation operation, HandlerMethod handlerMethod) {
                return operation.addParametersItem(
                        new Parameter().in("header").name(HttpHeaders.ETAG).required(false));
            }
        };
    }
}
