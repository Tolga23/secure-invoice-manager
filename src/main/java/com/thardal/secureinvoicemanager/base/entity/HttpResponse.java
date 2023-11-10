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

    public HttpResponse(HttpStatus status) {
        this.responseDate = new Date();
        this.status = status;
    }

    public HttpResponse(HttpStatus status, String message) {
        this(status);
        this.message = message;
    }

    public HttpResponse(HttpStatus status, String message, Map<?, ?> data) {
        this(status, message);
        this.data = data;
    }

    public HttpResponse(HttpStatus status, String message, String developerMessage) {
        this(status, message);
        this.developerMessage = developerMessage;
    }

    public HttpResponse(HttpStatus status, String message, String developerMessage, Map<?, ?> data) {
        this(status, message, developerMessage);
        this.data = data;
    }

    public static <T> HttpResponse<T> of(HttpStatus status, String message) {
        return new HttpResponse<>(status, message);
    }

    public static <T> HttpResponse<T> of(HttpStatus status, String message, Map<?, ?> data) {
        return new HttpResponse<>(status, message, data);
    }

    public static <T> HttpResponse<T> error(HttpStatus status, String message) {
        return new HttpResponse<>(status, message);
    }

    public static <T> HttpResponse<T> error(HttpStatus status, String message, Map<?, ?> data) {
        return new HttpResponse<>(status, message, data);
    }

    public static <T> HttpResponse<T> error(HttpStatus status, String message, String developerMessage) {
        return new HttpResponse<>(status, message, developerMessage);
    }

    public static <T> HttpResponse<T> error(HttpStatus status, String message, String developerMessage, Map<?, ?> data) {
        return new HttpResponse<>(status, message, developerMessage, data);
    }

    public static <T> HttpResponse<T> empty(HttpStatus status) {
        return new HttpResponse<>(status);
    }


}
