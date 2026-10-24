package backend.bookSharing.http.springdoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ModelResolver customModelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper) {
            @Override
            protected Schema.RequiredMode resolveRequiredMode(Schema schema) {
                // If the field has no annotation, or is explicitly set to AUTO, force REQUIRED
                if (schema == null || schema.requiredMode() == Schema.RequiredMode.AUTO) {
                    return Schema.RequiredMode.REQUIRED;
                }
                // Otherwise, respect explicit choices like Schema.RequiredMode.NOT_REQUIRED
                return schema.requiredMode();
            }
        };
    }

}
