package backend.bookSharing.http;

import backend.bookSharing.services.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
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

        return ProblemDetail.forStatusAndDetail(
                ex.getClass().getAnnotation(ResponseStatus.class).value(), ex.getClass().getName());
    }

    /**
     * Catches json parse error on missing values (interpreted as null)
     * This exception may be activated on other scenarios
     *
     * @param ex Exception
     * @return Problem Detail with message specific to the parameter violation detected from the request
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_CONTENT)
    public ProblemDetail handleJsonParseError(HttpMessageNotReadableException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
    }
}
