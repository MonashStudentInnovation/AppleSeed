package edu.monash.monplan.controller.request;

import org.hibernate.validator.constraints.NotBlank;

public class MessageTaskRequest {
    @NotBlank
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
