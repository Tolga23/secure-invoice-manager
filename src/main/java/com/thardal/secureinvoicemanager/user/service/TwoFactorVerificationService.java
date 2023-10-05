package com.thardal.secureinvoicemanager.user.service;

import com.thardal.secureinvoicemanager.user.service.entityservice.TwoFactorVerificationEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwoFactorVerificationService {

    private final TwoFactorVerificationEntityService verificationEntityService;

    public void deleteByUserId(Long userId) {
        verificationEntityService.deleteByUserId(userId);
    }

    public void updateByUserIdAndVerificationCodeAndExpirationDate(Long userId, String verificationCode, String expirationDate) {
        verificationEntityService.updateByUserIdAndVerificationCodeAndExpirationDate(userId, verificationCode, expirationDate);
    }

}
