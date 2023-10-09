package com.thardal.secureinvoicemanager.user.service.entityservice;

import com.thardal.secureinvoicemanager.base.service.BaseEntityService;
import com.thardal.secureinvoicemanager.user.entity.TwoFactorVerifications;
import com.thardal.secureinvoicemanager.user.repository.TwoFactorVerificationsRepositiory;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorVerificationEntityService extends BaseEntityService<TwoFactorVerifications, TwoFactorVerificationsRepositiory> {
    public TwoFactorVerificationEntityService(TwoFactorVerificationsRepositiory dao) {
        super(dao);
    }

    public void deleteByUserId(Long userId) {
        getDao().deleteByUserId(userId);
    }

    public void deleteByVerificationCode(String verificationCode) {
        getDao().deleteByVerificationCode(verificationCode);
    }

    public Long isVerificationCodeExpiredByCode(String verificationCode) {
        return getDao().isVerificationCodeExpiredByCode(verificationCode);
    }

    public void updateByUserIdAndVerificationCodeAndExpirationDate(Long userId, String verificationCode, String expirationDate) {
        getDao().updateByUserIdAndVerificationCodeAndExpirationDate(userId, verificationCode, expirationDate);
    }
}
