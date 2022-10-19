package com.vanthan.vn.dto;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private String code;
    private String message;
    private T body;

    public BaseResponse() {
        this.code = "00"; // default code  -> ENUM OR CONFIG
        this.message = "success";
        this.body = null;
    }

    public BaseResponse(T body) {
        this.code = "00"; // default code  -> ENUM OR CONFIG
        this.message = "success";
        this.body = body;
    }

    public BaseResponse(T body, String message) {
        this.code = "00";
        this.message = message;
        this.body = body;
    }

    public BaseResponse(String code, String message, T body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }
}
