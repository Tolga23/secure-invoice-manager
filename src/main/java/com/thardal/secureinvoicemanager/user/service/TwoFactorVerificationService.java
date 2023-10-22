package com.thardal.secureinvoicemanager.user.service;

import com.thardal.secureinvoicemanager.user.enums.UserErrorMessages;
import com.thardal.secureinvoicemanager.user.exception.ApiException;
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

    public void deleteByVerificationCode(String verificationCode) {
        verificationEntityService.deleteByVerificationCode(verificationCode);
    }

    public Long isVerificationCodeExpiredByCode(String verificationCode) {
        Long isVerificationCodeExpired = verificationEntityService.isVerificationCodeExpiredByCode(verificationCode);

        if (isVerificationCodeExpired == null) throw new ApiException(UserErrorMessages.INVALID_CODE);

        return isVerificationCodeExpired;
    }

    public void updateByUserIdAndVerificationCodeAndExpirationDate(Long userId, String verificationCode, String expirationDate) {
        verificationEntityService.updateByUserIdAndVerificationCodeAndExpirationDate(userId, verificationCode, expirationDate);
    }

}
