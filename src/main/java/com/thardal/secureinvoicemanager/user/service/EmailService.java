package com.thardal.secureinvoicemanager.user.service;

import com.thardal.secureinvoicemanager.user.enums.VerificationType;

public interface EmailService {
    void sendVerificationEmail(String firstName, String email, String verificationUrl, VerificationType verificationType);
}
