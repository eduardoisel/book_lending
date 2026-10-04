package backend.bookSharing.http;

import backend.bookSharing.services.ServiceException;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.providers.JavadocProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;

@Component
@RequiredArgsConstructor
public class ExceptionOperationCustomizer implements OperationCustomizer {

    @Autowired private final JavadocProvider javadocProvider;

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {

        Class<?>[] exceptionTypes = handlerMethod.getMethod().getExceptionTypes();

        if (operation.getResponses() == null) {
            operation.setResponses(new ApiResponses());
        }

        Arrays.stream(exceptionTypes).toList().stream()
                .filter(Class::isSealed)
                .filter(ServiceException.class::isAssignableFrom)
                .forEach(
                        parentException -> {
                            for (Class<?> ex : parentException.getPermittedSubclasses()) {

                                ResponseStatus annotation = ex.getAnnotation(ResponseStatus.class);

                                String status =
                                        (annotation != null)
                                                ? String.valueOf(annotation.value().value())
                                                : String.valueOf(HttpStatus.BAD_REQUEST);

                                if (operation.getResponses().containsKey(status)) {
                                    ApiResponse a = operation.getResponses().get(status);
                                    a.description(
                                            a.getDescription()
                                                    + ";\t"
                                                    + javadocProvider.getClassJavadoc(ex));

                                } else {
                                    Schema<?> schema =
                                            new Schema<>()
                                                    .$ref("#/components/schemas/ProblemDetail");
                                    MediaType mediaType = new MediaType().schema(schema);
                                    Content content =
                                            new Content()
                                                    .addMediaType(
                                                            "application/problem+json", mediaType);

                                    ApiResponse response =
                                            new ApiResponse()
                                                    .description(
                                                            javadocProvider.getClassJavadoc(ex))
                                                    .content(content);

                                    operation.getResponses().addApiResponse(status, response);
                                }
                            }
                        });

        return operation;
    }
}
