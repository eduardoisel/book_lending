package backend.bookSharing.http;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

/**
 * Reads SpringExprl from {@link PreAuthorize}.
 * Implementation expects the use of {@link SecurityExpressionRoot#hasRole(String)}
 */
@Component
public class ForbiddenOperationCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        // Find annotation on the method or the declaring controller class
        PreAuthorize securityAnnotation =
                AnnotatedElementUtils.findMergedAnnotation(
                        handlerMethod.getMethod(), PreAuthorize.class);

        if (securityAnnotation == null) {
            securityAnnotation =
                    AnnotatedElementUtils.findMergedAnnotation(
                            handlerMethod.getBeanType(), PreAuthorize.class);
        }

        if (securityAnnotation != null) {
            if (operation.getResponses() == null) {
                operation.setResponses(new ApiResponses());
            }

            Schema<?> schema = new Schema<>().$ref("#/components/schemas/ProblemDetail");

            MediaType mediaType = new MediaType().schema(schema);
            Content content = new Content().addMediaType("application/problem+json", mediaType);

            // role read expects the method hasRole
            String role = securityAnnotation.value().split("'")[1];

            ApiResponse forbiddenResponse =
                    new ApiResponse()
                            .description("Account needs to have the role: " + role)
                            .content(content);

            // Add or overwrite the 400 response code
            operation.getResponses().addApiResponse("403", forbiddenResponse);
        }

        return operation;
    }
}
