package backend.bookSharing.http;

import backend.bookSharing.services.book.failures.BookAdditionError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionConfig {

    public record ExceptionDto(String problem){}


    @ExceptionHandler(BookAdditionError.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionDto handleBookAdditionError(BookAdditionError ex) {
        return new ExceptionDto(ex.getClass().getSimpleName());
    }


}

