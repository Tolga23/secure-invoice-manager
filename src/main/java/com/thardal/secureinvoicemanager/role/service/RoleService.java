package com.thardal.secureinvoicemanager.role.service;

import com.thardal.secureinvoicemanager.base.enums.GlobalErrorMessages;
import com.thardal.secureinvoicemanager.base.exceptions.BusinessException;
import com.thardal.secureinvoicemanager.base.exceptions.NotFoundException;
import com.thardal.secureinvoicemanager.role.converter.RoleConverter;
import com.thardal.secureinvoicemanager.role.dto.RoleDto;
import com.thardal.secureinvoicemanager.role.entity.Role;
import com.thardal.secureinvoicemanager.role.enums.RoleErrorMessages;
import com.thardal.secureinvoicemanager.role.service.entityservice.RoleEntityService;
import com.thardal.secureinvoicemanager.user.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleEntityService roleEntityService;
    private final UserRolesService userRolesService;
    private final RoleConverter roleConverter;

    public Collection<RoleDto> getRoles(){
        Collection<Role> roles = roleEntityService.getRoles();
        List<RoleDto> dtoList = roleConverter.toDtoList((List<Role>) roles);

        return dtoList;
    }

    public void addRoleToUser(Long userId, String roleName){
        Long roleId = findRoleIdByRoleName(roleName);

        userRolesService.addUserRole(userId,roleId);
    }

    public void updateRoleToUser(Long userId, String roleName){
        Long roleId = findRoleIdByRoleName(roleName);
        userRolesService.updateUserRole(userId,roleId);
    }

    public Long findRoleIdByRoleName(String roleName) {
        Role role = roleEntityService.findRoleByRoleName(roleName);
        return role.getId();
    }

    public RoleDto getRoleByUserId(Long userId){

        try {
            Role role = roleEntityService.getRoleByUserId(userId);

            RoleDto roleDto = roleConverter.toDto(role);
            log.info("Roles found: " + roleDto.toString());
            return roleDto;

        }catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(RoleErrorMessages.ROLE_NOT_FOUND);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BusinessException(GlobalErrorMessages.ERROR_OCCURRED);
        }

    }

}
