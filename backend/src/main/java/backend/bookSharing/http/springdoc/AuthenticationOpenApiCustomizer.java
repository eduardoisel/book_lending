package backend.bookSharing.http.springdoc;

import backend.bookSharing.http.AuthorizationEndpoints;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import java.util.Arrays;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.ISBN;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
@RequiredArgsConstructor
public class AuthenticationOpenApiCustomizer implements OpenApiCustomizer {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    {
        antPathMatcher.setCaseSensitive(false);
    }

    @Override
    public void customise(OpenAPI openApi) {

        openApi.getPaths()
                .forEach(
                        (path, pathItem) -> {
                            if (pathItem.getDelete() != null) {
                                setOperationSecurity(path, HttpMethod.DELETE, pathItem.getDelete());
                            }

                            if (pathItem.getGet() != null) {
                                setOperationSecurity(path, HttpMethod.GET, pathItem.getGet());
                            }
                            if (pathItem.getPost() != null) {
                                setOperationSecurity(path, HttpMethod.POST, pathItem.getPost());
                            }
                            if (pathItem.getPut() != null) {
                                setOperationSecurity(path, HttpMethod.PUT, pathItem.getPut());
                            }
                        });
    }

    private void setOperationSecurity(String path, HttpMethod httpMethod, Operation operation) {

        for (AuthorizationEndpoints.Permission permission : AuthorizationEndpoints.permissions) {
            if (permission.httpMethod().equals(httpMethod)
                    && antPathMatcher.match(permission.pattern(), path)) {
                switch (permission.access()) {
                    case AuthorizationEndpoints.ImposePermission.Authenticated _ -> {
                        operation.addSecurityItem(new SecurityRequirement().addList("bearer"));

                        // add unauthorized response
                        if (operation.getResponses() == null) {
                            operation.setResponses(new ApiResponses());
                        }

                        Schema<?> schema =
                                new Schema<>().$ref("#/components/schemas/ProblemDetail");

                        MediaType mediaType = new MediaType().schema(schema);
                        Content content =
                                new Content().addMediaType("application/problem+json", mediaType);

                        ApiResponse forbiddenResponse =
                                new ApiResponse()
                                        .description("Account needs bearer authentication")
                                        .content(content);

                        operation.getResponses().addApiResponse("401", forbiddenResponse);

                        // add authorization header as path parameter

                        LinkedList<Parameter> parameters =
                                (operation.getParameters() != null)
                                        ? new LinkedList<>(operation.getParameters())
                                        : new LinkedList<>();
                        Parameter authorizationHeaderParameter =
                                new Parameter()
                                        .name("Authorization")
                                        .description("Authorization header")
                                        .in("header")
                                        .schema(new StringSchema())
                                        .required(true);
                        parameters.add(authorizationHeaderParameter);
                        operation.setParameters(parameters);
                    }
                    case AuthorizationEndpoints.ImposePermission.PermitAll _ -> {}
                }
                return;
            }
        }
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
        return new PropertyCustomizer() {

            @Override
            public Schema<?> customize(Schema property, AnnotatedType type) {
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
            }
        };
    }
}
