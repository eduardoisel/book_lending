package backend.bookSharing.http.springdoc;

import backend.bookSharing.http.AuthorizationEndpoints;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
@RequiredArgsConstructor
public class AuthenticationOpenApiCustomizer implements OpenApiCustomizer {

    private final AntPathMatcher antPathMatcher;

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

        Predicate<AuthorizationEndpoints.Permission> findMatch =
                permission ->
                        permission.httpMethod().equals(httpMethod)
                                && antPathMatcher.match(permission.pattern(), path);

        Optional<AuthorizationEndpoints.Permission> optionalPermission =
                findAnyMatch(Arrays.stream(AuthorizationEndpoints.permissions), findMatch);

        if (optionalPermission.isEmpty()) {
            return;
        }

        AuthorizationEndpoints.Permission permission = optionalPermission.get();

        switch (permission.access()) {
            case AuthorizationEndpoints.ImposePermission.Authenticated _ -> {
                operation.addSecurityItem(
                        new SecurityRequirement()
                                .addList(SpringDocConfiguration.BEARER_SECURITY_SCHEME));

                // add unauthorized response
                if (operation.getResponses() == null) {
                    operation.setResponses(new ApiResponses());
                }

                Schema<?> schema = new Schema<>().$ref("#/components/schemas/ProblemDetail");

                MediaType mediaType = new MediaType().schema(schema);
                Content content = new Content().addMediaType("application/problem+json", mediaType);

                ApiResponse forbiddenResponse =
                        new ApiResponse()
                                .description("Account needs bearer authentication")
                                .content(content);

                operation
                        .getResponses()
                        .addApiResponse(
                                String.valueOf(HttpStatus.UNAUTHORIZED.value()), forbiddenResponse);

                // add authorization header as path parameter
                /*
                Parameter authorizationHeaderParameter =
                        new Parameter()
                                .name("Authorization")
                                .description("Authorization header")
                                .in("header")
                                .schema(new Schema<>().$ref("#/components/headers/Authorization"))
                                .required(true);
                operation.addParametersItem(authorizationHeaderParameter);
                */
            }
            case AuthorizationEndpoints.ImposePermission.PermitAll _ -> {}
        }
    }

    private <T> Optional<T> findAnyMatch(Stream<T> stream, Predicate<T> predicate) {
        return stream.reduce((current, next) -> predicate.test(current) ? current : next);
    }
}
