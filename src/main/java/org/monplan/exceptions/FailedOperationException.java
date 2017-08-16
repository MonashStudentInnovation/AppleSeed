package org.monplan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FailedOperationException extends Exception {
    public FailedOperationException() {
        super();
    }
    public FailedOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    public FailedOperationException(String message) {
        super(message);
    }
    public FailedOperationException(Throwable cause) {
        super(cause);
    }
}
