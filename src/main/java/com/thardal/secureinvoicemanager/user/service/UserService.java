package com.thardal.secureinvoicemanager.user.service;

import com.thardal.secureinvoicemanager.user.converter.UserConverter;
import com.thardal.secureinvoicemanager.user.dto.UserDto;
import com.thardal.secureinvoicemanager.user.dto.UserSaveRequestDto;
import com.thardal.secureinvoicemanager.user.entity.User;
import com.thardal.secureinvoicemanager.user.exception.ApiException;
import com.thardal.secureinvoicemanager.user.service.entityservice.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityService userEntityService;
    private final UserConverter userConverter;

    public List<UserDto> findAll() {
        List<User> userList = userEntityService.findAll();

        List<UserDto> userDtoList = userConverter.toDtoList(userList);

        return userDtoList;
    }

    public UserDto save(UserSaveRequestDto userSaveRequestDto) {

        User user = userConverter.convertToUserSave(userSaveRequestDto);

        // Check email is unique
        String trimMail = getTrimmedMail(user);
        if (existsByEmail(trimMail) == true)
            throw new ApiException("Email already in use. Please use a different email adress");

        // Save new user
        user = userEntityService.save(user);

        UserDto userDto = userConverter.toDto(user);

        // Add role to the user
        // Send verification URL
        // Save URL in verification table
        // Send email to user with verification URL
        // Return newly created user
        // if any errors, throw exception with proper message

        return userDto;
    }

    private boolean existsByEmail(String trimMail) {
        boolean isEmailExist = userEntityService.existsByEmail(trimMail);

        return isEmailExist;
    }

    private String getTrimmedMail(User user) {
        String email = user.getEmail();
        String trimmedMail = email.trim().toLowerCase();
        return trimmedMail;
    }
}
