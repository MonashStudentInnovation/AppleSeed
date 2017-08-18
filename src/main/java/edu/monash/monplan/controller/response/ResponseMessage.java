package edu.monash.monplan.controller.response;

import org.hibernate.validator.constraints.NotBlank;

public class ResponseMessage {
    @NotBlank
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}