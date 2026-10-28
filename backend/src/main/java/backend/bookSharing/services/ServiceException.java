package backend.bookSharing.services;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base class for identifying them all easily
 * <p>
 * At least for now it is assumed the subclass has annotations for the necessary information,
 * such as {@link ResponseStatus}
 */
@RequiredArgsConstructor
public abstract class ServiceException extends Exception {

    // public final HttpStatus httpStatus;

    // public final String detail;

}
