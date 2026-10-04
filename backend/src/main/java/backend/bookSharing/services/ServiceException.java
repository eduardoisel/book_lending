package backend.bookSharing.services;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base class for identifying them all easily
 * <p>
 * At least for now it is assumed the subclass has annotations for the necessary information,
 * such as {@link ResponseStatus}
 */
public abstract class ServiceException extends Exception {

    //    public abstract HttpStatus getHttpStatus();
    //
    //    public String type() {
    //        return "type-template";
    //    }
}
