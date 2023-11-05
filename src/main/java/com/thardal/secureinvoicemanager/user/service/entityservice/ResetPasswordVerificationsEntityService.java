package com.thardal.secureinvoicemanager.user.service.entityservice;

import com.thardal.secureinvoicemanager.base.service.BaseEntityService;
import com.thardal.secureinvoicemanager.user.entity.ResetPasswordVerifications;
import com.thardal.secureinvoicemanager.user.repository.ResetPasswordVerificationsRepository;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordVerificationsEntityService extends BaseEntityService<ResetPasswordVerifications, ResetPasswordVerificationsRepository> {
    public ResetPasswordVerificationsEntityService(ResetPasswordVerificationsRepository dao) {
        super(dao);
    }

    public void deleteResetPasswordVerificationsByUserId(Long userId){
        getDao().deleteResetPasswordVerificationsByUserId(userId);
    }

    public void insertResetPasswordVerifications(Long userId,String expirationDate,String url){
        getDao().insertResetPasswordVerifications(userId,expirationDate,url);
    }

    public Long isLinkExpired(String url){
        return getDao().isUrlExpired(url);
    }

}
