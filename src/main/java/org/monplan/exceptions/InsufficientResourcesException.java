package org.monplan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class InsufficientResourcesException extends Exception {
    public InsufficientResourcesException() {
        super();
    }
    public InsufficientResourcesException(String message, Throwable cause) {
        super(message, cause);
    }
    public InsufficientResourcesException(String message) {
        super(message);
    }
    public InsufficientResourcesException(Throwable cause) {
        super(cause);
    }
}
