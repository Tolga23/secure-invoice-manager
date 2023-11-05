package com.thardal.secureinvoicemanager.user.service;

import com.thardal.secureinvoicemanager.user.service.entityservice.ResetPasswordVerificationsEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordVerificationsService {
    private final ResetPasswordVerificationsEntityService resetPasswordEntityService;

    public void deleteResetPasswordVerificationsByUserId(Long userId){
        resetPasswordEntityService.deleteResetPasswordVerificationsByUserId(userId);
    }
    public void insertResetPasswordVerifications(Long userId,String expirationDate,String url){
        resetPasswordEntityService.insertResetPasswordVerifications(userId,expirationDate,url);
    }

    public Long isLinkExpired(String url){
        return resetPasswordEntityService.isLinkExpired(url);
    }
}
