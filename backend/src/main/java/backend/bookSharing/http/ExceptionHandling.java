package backend.bookSharing.http;

import backend.bookSharing.http.returns.ValidationProblemDetail;
import backend.bookSharing.services.ServiceException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

/**
 * Does not extend {@link org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler} since
 * its catch-all handleException conflicts with these independent ones. Overriding its methods is possible, but a bit too
 * cumbersome and I believe incapable of being recognized by springdoc due to them not having {@link ResponseStatus}.
 */
@SuppressWarnings("unused")
@ControllerAdvice
public class ExceptionHandling { // extends ResponseEntityExceptionHandler {

    /*
    To enforce RFC 9457 for all exceptions

    todo add handler for repo exceptions specifically
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleThrowable(Throwable ex) {
        ProblemDetail detail =
                ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        detail.setTitle(ex.getClass().getName());
        return detail;
    }

    /**
     * validation exception thrown by spring controller related to {@code jakarta.validation} annotations.
     * Only work on parameters of classes, not on annotations used directly on base values (strings and primitives)
     *
     * @param ex list of constraints with the parameter name attached to the requirement message
     * @return Problem Detail with message specific to the parameter violations detected from the request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ValidationProblemDetail handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        // HandlerMethodValidationException
        List<ValidationProblemDetail.FieldError> fieldErrors =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(ValidationProblemDetail.FieldError::new)
                        .toList();

        return new ValidationProblemDetail(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "validation issue",
                URI.create("todo-replace"),
                fieldErrors);
    }

    /**
     * Value validation for basic values (primitives and wrappers, strings)
     * @param ex list of constraints with the parameter name attached to the requirement message
     * @return Validation Problem detail with message specific to the parameter violations detected from the request
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ValidationProblemDetail handleHandlerMethodValidation(
            HandlerMethodValidationException ex) {

        LinkedList<ValidationProblemDetail.FieldError> fieldErrors = new LinkedList<>();

        ex.getParameterValidationResults()
                .forEach(
                        parameterValidationResult -> {
                            String paramName =
                                    parameterValidationResult
                                            .getMethodParameter()
                                            .getParameterName();
                            fieldErrors.addAll(
                                    parameterValidationResult.getResolvableErrors().stream()
                                            .map(
                                                    err ->
                                                            new ValidationProblemDetail.FieldError(
                                                                    paramName,
                                                                    err.getDefaultMessage()))
                                            .toList());
                        });

        return new ValidationProblemDetail(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "validation issue",
                URI.create("todo-replace"),
                fieldErrors);
    }

//    /**
//     *
//     * validation exception, this one triggered by validations done on the server
//     * @param ex list of constraints with the parameter name attached to the requirement message
//     * @return Validation Problem Detail with message specific to the parameter violations detected from the request
//     */
//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseBody
//    public ValidationProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
//
//        LinkedList<ValidationProblemDetail.FieldError> fieldErrors = new LinkedList<>();
//
//        ex.getConstraintViolations()
//                .forEach(
//                        violation -> {
//                            Path path = violation.getPropertyPath();
//
//                            String fieldName = null;
//
//                            /*
//                            Nodes go from method node to parameter node (and if param is data class goes to its
//                            fields until is the one with the violation)
//                             */
//                            for (Path.Node node : path) {
//                                fieldName = node.getName();
//                            }
//
//                            if (fieldName == null) {
//                                throw new IllegalStateException(
//                                        "Path node field is null, should have been replaced");
//                            }
//
//                            fieldErrors.add(
//                                    new ValidationProblemDetail.FieldError(
//                                            fieldName, violation.getMessage()));
//                        });
//
//        return new ValidationProblemDetail(
//                HttpStatus.UNPROCESSABLE_CONTENT.value(),
//                "validation issue",
//                URI.create("todo-replace"),
//                fieldErrors);
//    }

    /**
     *
     * @param ex Authorization denied Exception assumed to be thrown by spring security such as in
     *           {@link PreAuthorize}
     * @return ProblemDetail with a detail message from the exception
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ProblemDetail handleException(Exception ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public ProblemDetail handleServiceException(ServiceException ex) {

        return ProblemDetail.forStatusAndDetail(
                ex.getClass().getAnnotation(ResponseStatus.class).value(), ex.getClass().getName());
    }
}
