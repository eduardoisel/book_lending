package backend.bookSharing.http;

import backend.bookSharing.http.returns.ValidationProblemDetail;
import backend.bookSharing.services.ServiceException;
import java.net.URI;
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

/**
 * The spring way to handle exceptions
 * To use it uncomment teh class annotations, and do not handle the exception directly on the controller
 */
@ControllerAdvice
public class ExceptionHandling {

    /*
    To enforce RFC 9457 for all exceptions
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleThrowable(Throwable ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        detail.setTitle(ex.getClass().getName());
        return detail;
    }

    /**
     * Catches exceptions thrown by spring related to {@code jakarta.validation}, which includes things such as the
     * acceptable size for strings, the max and min values of numbers, and so on
     *
     * @param ex Exception
     * @return Problem Detail with message specific to the parameter violation detected from the request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

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
        // ProblemDetail detail = ProblemDetail.forStatusAndDetail(ex.getHttpStatus(),
        // ex.getMessage());

        //        if (ex instanceof ValidationProblem) {
        //            ValidationProblem<?> problem = (ValidationProblem<?>) ex;
        //
        //            //problem.
        //
        //        }

        return ProblemDetail.forStatusAndDetail(
                ex.getClass().getAnnotation(ResponseStatus.class).value(), ex.getClass().getName());
    }
}
