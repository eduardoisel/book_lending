package backend.bookSharing.http.returns;

import java.net.URI;
import java.util.List;
import org.springframework.http.ProblemDetail;

public class ValidationProblemDetail extends ProblemDetail {

    public record FieldError(String field, String message) {
        public FieldError(org.springframework.validation.FieldError fieldError) {
            this(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }

    public final List<FieldError> validationIssues;

    public ValidationProblemDetail(
            Integer status, String title, URI type, List<FieldError> validationIssues) {
        super();
        this.setStatus(status);
        this.setTitle(title);
        this.setType(type);
        this.validationIssues = validationIssues;
        // this.setProperty("TEsting", "Heloooo");
    }
}
