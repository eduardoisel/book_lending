package backend.bookSharing.http;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
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
                    case AuthorizationEndpoints.ImposePermission.Authenticated _ ->
                            operation.addSecurityItem(new SecurityRequirement().addList("bearer"));
                    case AuthorizationEndpoints.ImposePermission.PermitAll _ -> {}
                }
                return;
            }
        }
    }
}
