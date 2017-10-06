package edu.monash.monplan.controller.response;

import org.hibernate.validator.constraints.NotBlank;

public class ResponseDataWithPages {
    @NotBlank
    private Object data;
    private int totalPages;

    public ResponseDataWithPages(Object data, int totalPages) {
        this.data = data;
        this.totalPages = totalPages;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}