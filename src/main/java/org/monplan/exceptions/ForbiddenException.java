package org.monplan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends Exception{
    public ForbiddenException() {
        super();
    }
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
    public ForbiddenException(String message) {
        super(message);
    }
    public ForbiddenException(Throwable cause) {
        super(cause);
    }
}
