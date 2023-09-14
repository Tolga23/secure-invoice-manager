package com.thardal.secureinvoicemanager.role.service.entityservice;

import com.thardal.secureinvoicemanager.base.service.BaseEntityService;
import com.thardal.secureinvoicemanager.role.entity.UserRoles;
import com.thardal.secureinvoicemanager.role.repository.UserRolesRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRolesEntityService extends BaseEntityService<UserRoles, UserRolesRepository> {
    public UserRolesEntityService(UserRolesRepository dao) {
        super(dao);
    }
}
