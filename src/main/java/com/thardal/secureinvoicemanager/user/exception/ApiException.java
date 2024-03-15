package com.thardal.secureinvoicemanager.user.exception;

import com.thardal.secureinvoicemanager.base.enums.BaseErrorMessages;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ApiException extends RuntimeException {
    private final BaseErrorMessages baseErrorMessages;
}
