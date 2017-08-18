package org.monplan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorisedException extends Exception {
    public UnauthorisedException() {
        super();
    }
    public UnauthorisedException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnauthorisedException(String message) {
        super(message);
    }
    public UnauthorisedException(Throwable cause) {
        super(cause);
    }
}
