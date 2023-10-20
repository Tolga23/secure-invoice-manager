package com.thardal.secureinvoicemanager.role.service.entityservice;

import com.thardal.secureinvoicemanager.base.service.BaseEntityService;
import com.thardal.secureinvoicemanager.role.entity.Role;
import com.thardal.secureinvoicemanager.role.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleEntityService extends BaseEntityService<Role, RoleRepository> {
    public RoleEntityService(RoleRepository dao) {
        super(dao);
    }

    public void updateRoleByIdAndRoleName(Long userId, String roleName) {
        getDao().updateRoleByIdAndRoleName(userId, roleName);
    }

    public Role getRoleByUserId(Long userId) {
        return getDao().getRoleByUserId(userId);
    }

    public Role findRoleByRoleName(String roleName) {
        return getDao().findRoleByRoleName(roleName);
    }

}
