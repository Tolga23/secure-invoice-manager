package com.thardal.secureinvoicemanager.base.exceptions;

import com.thardal.secureinvoicemanager.base.entity.HttpResponse;
import com.thardal.secureinvoicemanager.user.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static com.thardal.secureinvoicemanager.user.enums.UserErrorMessages.BAD_CREDENTIALS;

@RestController
@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler implements ErrorController {

    @ExceptionHandler
    public ResponseEntity<Object> handleException(Exception ex) {
        String errorMessage = ex.getMessage().contains("expected 1, actual 0") ? "Record not found" : "Some error occurred";
        HttpResponse<Object> restResponse = HttpResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        restResponse.setDeveloperMessage(ex.getMessage());
        return new ResponseEntity<>(restResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        String fieldMessage = fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        HttpResponse<Object> response = HttpResponse.error(HttpStatus.resolve(status.value()), fieldMessage);

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        String errorMessage = "An business error occurred: " + ex.getBaseErrorMessages().getMessage();
        HttpResponse<Object> restResponse = HttpResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);


        return new ResponseEntity<>(restResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex) {
        String errorMessage = "An API error occurred: " + ex.getBaseErrorMessages().getMessage();
        HttpResponse<Object> restResponse = HttpResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);

        return new ResponseEntity<>(restResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleCustomExcepiton(NotFoundException ex) {
        String errorMessage = "An error occurred: " + ex.getBaseErrorMessages().getMessage();

        HttpResponse<Object> restResponse = HttpResponse.error(HttpStatus.NOT_FOUND, errorMessage);

        return new ResponseEntity<>(restResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String errorMessage = "Data integrity violation: " + ex.getMessage();
        HttpResponse<Object> restResponse = HttpResponse.error(HttpStatus.CONFLICT, errorMessage);

        return new ResponseEntity<>(restResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException(BadCredentialsException ex) {
        String errorMessage = ex.getMessage() + ", " + BAD_CREDENTIALS;
        HttpResponse<Object> restResponse = HttpResponse.error(HttpStatus.BAD_REQUEST, errorMessage);

        return new ResponseEntity<>(restResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<HttpResponse> authException(AuthenticationException ex) {
        String errorMessage = "" + BAD_CREDENTIALS;
        HttpResponse<Object> restResponse = HttpResponse.error(HttpStatus.UNAUTHORIZED, errorMessage);

        return new ResponseEntity<>(restResponse, HttpStatus.UNAUTHORIZED);
    }

}
