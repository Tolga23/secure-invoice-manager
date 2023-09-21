package com.thardal.secureinvoicemanager.user.service.entityservice;

import com.thardal.secureinvoicemanager.base.service.BaseEntityService;
import com.thardal.secureinvoicemanager.user.entity.UserVerification;
import com.thardal.secureinvoicemanager.user.repository.UserVerificationRepository;
import org.springframework.stereotype.Service;

@Service
public class UserVerificationEntityService extends BaseEntityService<UserVerification, UserVerificationRepository> {
    public UserVerificationEntityService(UserVerificationRepository dao) {
        super(dao);
    }

}
