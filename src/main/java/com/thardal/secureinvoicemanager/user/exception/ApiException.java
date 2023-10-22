package com.thardal.secureinvoicemanager.user.exception;

import com.thardal.secureinvoicemanager.base.enums.BaseErrorMessages;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ApiException extends RuntimeException {
    private final BaseErrorMessages baseErrorMessages;
}
