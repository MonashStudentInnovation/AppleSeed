package edu.monash.monplan.controller.response;

import org.hibernate.validator.constraints.NotBlank;

public class ResponseData {
    @NotBlank
    private Object data;

    public ResponseData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}