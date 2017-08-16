package edu.monash.monplan.controller.response;

import org.hibernate.validator.constraints.NotBlank;

public class ResponseMessage {
    @NotBlank
    private String message;

    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}