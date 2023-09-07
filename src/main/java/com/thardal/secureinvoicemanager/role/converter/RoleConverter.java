package com.thardal.secureinvoicemanager.role.converter;

import com.thardal.secureinvoicemanager.base.converter.BaseConverter;
import com.thardal.secureinvoicemanager.role.dto.RoleDto;
import com.thardal.secureinvoicemanager.role.entity.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleConverter implements BaseConverter<Role, RoleDto> {

    @Override
    public RoleDto toDto(Role role) {
        if (role == null) return null;

        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setRoleName(role.getRoleName());
        roleDto.setPermission(role.getPermission());

        return roleDto;
    }

    @Override
    public Role toEntity(RoleDto roleDto) {
        if (roleDto == null) return null;

        Role role = new Role();
        role.setId(roleDto.getId());
        role.setRoleName(roleDto.getRoleName());
        role.setPermission(roleDto.getPermission());

        return role;
    }

    @Override
    public List<Role> toEntityList(List<RoleDto> roleDtoList) {
        if (roleDtoList == null) return null;

        List<Role> roleList = new ArrayList<>(roleDtoList.size());
        for(RoleDto roleDto  : roleDtoList) {
            Role role = toEntity(roleDto);
            roleList.add(role);
        }

        return roleList;
    }

    @Override
    public List<RoleDto> toDtoList(List<Role> roleList) {
        if(roleList == null) return null;

        List<RoleDto> roleDtoList = new ArrayList<>(roleList.size());
        for (Role role : roleList) {
            RoleDto roleDto = toDto(role);
            roleDtoList.add(roleDto);
        }

        return roleDtoList;
    }
}
