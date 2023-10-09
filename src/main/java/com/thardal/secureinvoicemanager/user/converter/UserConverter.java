package com.thardal.secureinvoicemanager.user.converter;

import com.thardal.secureinvoicemanager.base.converter.BaseConverter;
import com.thardal.secureinvoicemanager.role.dto.RoleDto;
import com.thardal.secureinvoicemanager.user.dto.UserDto;
import com.thardal.secureinvoicemanager.user.dto.UserSaveRequestDto;
import com.thardal.secureinvoicemanager.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter implements BaseConverter<User, UserDto> {
    @Override
    public UserDto toDto(User user) {
        UserDto dto = UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .title(user.getTitle())
                .bio(user.getBio())
                .enable(user.isEnable())
                .imageUrl(user.getImageUrl())
                .isNotLocked(user.isNotLocked())
                .isUsingAuth(user.isUsingAuth())
                .build();

        return dto;
    }

    @Override
    public User toEntity(UserDto userDto) {
        User user = User.builder()
                .id(userDto.getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .address(userDto.getAddress())
                .title(userDto.getTitle())
                .bio(userDto.getBio())
                .enable(userDto.isEnable())
                .imageUrl(userDto.getImageUrl())
                .isNotLocked(userDto.isNotLocked())
                .isUsingAuth(userDto.isUsingAuth())
                .build();

        return user;
    }

    @Override
    public List<User> toEntityList(List<UserDto> userDtoList) {
        List<User> userList = userDtoList.stream().map(userDto -> toEntity(userDto)).collect(Collectors.toList());

        return userList;
    }

    @Override
    public List<UserDto> toDtoList(List<User> userList) {
        List<UserDto> userDtoList = userList.stream().map(user -> toDto(user)).collect(Collectors.toList());

        return userDtoList;
    }

    public User convertToUserSave(UserSaveRequestDto userSaveRequestDto) {
        User user = User.builder()
                .firstName(userSaveRequestDto.getFirstName())
                .lastName(userSaveRequestDto.getLastName())
                .email(userSaveRequestDto.getEmail())
                .password(userSaveRequestDto.getPassword())
                .address(userSaveRequestDto.getAddress())
                .phone(userSaveRequestDto.getPhone())
                .title(userSaveRequestDto.getTitle())
                .bio(userSaveRequestDto.getBio())
                .imageUrl(userSaveRequestDto.getImageUrl())
                .build();

        return user;
    }

    public UserDto userAndRoleDto(User user, RoleDto role) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .title(user.getTitle())
                .bio(user.getBio())
                .enable(user.isEnable())
                .imageUrl(user.getImageUrl())
                .isNotLocked(user.isNotLocked())
                .isUsingAuth(user.isUsingAuth())
                .roleName(role.getRoleName())
                .permissions(role.getPermission())
                .build();

        return userDto;
    }

}
