package com.thardal.secureinvoicemanager.user.converter;

import com.thardal.secureinvoicemanager.base.converter.BaseConverter;
import com.thardal.secureinvoicemanager.user.dto.UserVerificationDto;
import com.thardal.secureinvoicemanager.user.dto.UserVerificationSaveRequestDto;
import com.thardal.secureinvoicemanager.user.entity.UserVerification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserVerificationConverter implements BaseConverter<UserVerification, UserVerificationDto> {
    @Override
    public UserVerificationDto toDto(UserVerification userVerification) {
        UserVerificationDto userVerificationDto = UserVerificationDto.builder()
                .id(userVerification.getId())
                .userId(userVerification.getUserId())
                .url(userVerification.getUrl())
                .build();


        return userVerificationDto;
    }

    @Override
    public UserVerification toEntity(UserVerificationDto userVerificationDto) {
        return null;
    }

    @Override
    public List<UserVerification> toEntityList(List<UserVerificationDto> d) {
        return null;
    }

    @Override
    public List<UserVerificationDto> toDtoList(List<UserVerification> e) {
        return null;
    }

    public UserVerification convertToUserVerificationSaveRequestToUserVerification(UserVerificationSaveRequestDto userVerificationSaveRequestDto) {
        UserVerification userVerification = UserVerification.builder()
                .userId(userVerificationSaveRequestDto.getUserId())
                .url(userVerificationSaveRequestDto.getUrl())
                .build();

        return userVerification;
    }
}
