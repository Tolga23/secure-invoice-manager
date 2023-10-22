package com.thardal.secureinvoicemanager.base.exceptions;

import com.thardal.secureinvoicemanager.base.enums.BaseErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends BusinessException{
    public NotFoundException(BaseErrorMessages message) {
        super(message);
    }
}
