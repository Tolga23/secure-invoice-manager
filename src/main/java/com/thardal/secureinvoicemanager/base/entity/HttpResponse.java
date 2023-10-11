package com.thardal.secureinvoicemanager.base.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@SuperBuilder
public class HttpResponse<T> implements Serializable {
    protected Date responseDate;
    protected int statusCode;
    protected HttpStatus status;
    protected String reason;
    protected String message;
    protected String developerMessage;
    protected Map<?, ?> data;

    public HttpResponse(int statusCode, HttpStatus status, Map<?, ?> data) {
        this.responseDate = new Date();
        this.statusCode = statusCode;
        this.status = status;
        this.data = data;
    }

    public HttpResponse(int statusCode, HttpStatus status, String message, Map<?, ?> data) {
        this.responseDate = new Date();
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public HttpResponse(int statusCode, HttpStatus status, String reason) {
        this.responseDate = new Date();
        this.statusCode = statusCode;
        this.status = status;
        this.reason = reason;
    }

    public HttpResponse(int statusCode, HttpStatus status) {
        this.responseDate = new Date();
        this.statusCode = statusCode;
        this.status = status;
    }


    public static <T> HttpResponse<T> of(int statusCode, HttpStatus status, Map<?, ?> data) {
        return new HttpResponse<>(statusCode, status, data);
    }

    public static <T> HttpResponse<T> of(int statusCode, HttpStatus status, String message, Map<?, ?> data) {
        return new HttpResponse<>(statusCode, status, message, data);
    }

    public static <T> HttpResponse<T> error(int statusCode, HttpStatus status, String reason) {
        return new HttpResponse<>(statusCode, status, reason);
    }

    public static <T> HttpResponse<T> error(int statusCode, HttpStatus status, Map<?, ?> data) {
        return new HttpResponse<>(statusCode, status, data);
    }

    public static <T> HttpResponse<T> error(int statusCode, HttpStatus status, String message, Map<?, ?> data) {
        return new HttpResponse<>(statusCode, status, message, data);
    }

    public static <T> HttpResponse<T> empty(int statusCode, HttpStatus status) {
        return new HttpResponse<>(statusCode, status);
    }


}
