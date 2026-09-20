package backend.bookSharing.http;

import backend.bookSharing.services.book.failures.BookAdditionError;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
The spring way to handle exceptions, not used but here as the most basic example

To use it uncomment teh class annotations, and do not handle the exception directly on the controller
 */
@ControllerAdvice
public class ExceptionHandling {

    public record ExceptionDto(String problem) {}

    @ExceptionHandler(BookAdditionError.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionDto handleBookAdditionError(BookAdditionError ex) {
        return new ExceptionDto(ex.getClass().getSimpleName());
    }

    /*
    Catches json parse error on missing values (interpreted as null)
    This exception may be activated on other scenarios
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionDto handleJsonParseError(HttpMessageNotReadableException ex) {
        return new ExceptionDto(ex.getMessage());
    }
}
