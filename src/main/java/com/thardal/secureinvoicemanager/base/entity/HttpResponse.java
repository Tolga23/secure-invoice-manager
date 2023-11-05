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
    protected HttpStatus status;
    protected String message;
    protected String developerMessage;
    protected Map<?, ?> data;

    public HttpResponse(HttpStatus status, Map<?, ?> data) {
        this.responseDate = new Date();
        this.status = status;
        this.data = data;
    }

    public HttpResponse(HttpStatus status, String message, Map<?, ?> data) {
        this.responseDate = new Date();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public HttpResponse(HttpStatus status) {
        this.responseDate = new Date();
        this.status = status;
    }

    public HttpResponse( HttpStatus status, String message) {
        this.responseDate = new Date();
        this.status = status;
        this.message = message;
    }

    public static <T> HttpResponse<T> of(HttpStatus status, Map<?, ?> data) {
        return new HttpResponse<>(status, data);
    }

    public static <T> HttpResponse<T> of(HttpStatus status, String message, Map<?, ?> data) {
        return new HttpResponse<>(status, message, data);
    }

    public static <T> HttpResponse<T> of(HttpStatus status, String message) {
        return new HttpResponse<>(status, message);
    }

    public static <T> HttpResponse<T> error(HttpStatus status, String message, Map<?, ?> data) {
        return new HttpResponse<>(status, message, data);
    }

    public static <T> HttpResponse<T> error(HttpStatus status, String message) {
        return new HttpResponse<>(status, message);
    }

    public static <T> HttpResponse<T> empty(HttpStatus status) {
        return new HttpResponse<>(status);
    }


}
