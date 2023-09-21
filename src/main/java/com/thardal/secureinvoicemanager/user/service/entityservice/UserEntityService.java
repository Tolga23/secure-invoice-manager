package com.thardal.secureinvoicemanager.user.service.entityservice;

import com.thardal.secureinvoicemanager.base.service.BaseEntityService;
import com.thardal.secureinvoicemanager.user.entity.User;
import com.thardal.secureinvoicemanager.user.repository.UserRepository;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.stereotype.Service;

@Service
public class UserEntityService extends BaseEntityService<User, UserRepository> {
    public UserEntityService(UserRepository dao) {
        super(dao);
    }

}
