package com.thardal.secureinvoicemanager.user.service;

import com.thardal.secureinvoicemanager.user.converter.UserVerificationConverter;
import com.thardal.secureinvoicemanager.user.dto.UserVerificationDto;
import com.thardal.secureinvoicemanager.user.dto.UserVerificationSaveRequestDto;
import com.thardal.secureinvoicemanager.user.entity.UserVerification;
import com.thardal.secureinvoicemanager.user.service.entityservice.UserVerificationEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserVerificationService {
    private final UserVerificationEntityService userVerificationEntityService;
    private final UserVerificationConverter userVerificationConverter;

    public UserVerificationDto save(UserVerificationSaveRequestDto userVerificationSaveRequestDto){

        UserVerification userVerification = userVerificationConverter.convertToUserVerificationSaveRequestToUserVerification(userVerificationSaveRequestDto);

        userVerification = userVerificationEntityService.save(userVerification);

        UserVerificationDto userVerificationDto = userVerificationConverter.toDto(userVerification);

        return userVerificationDto;
    }

    public void createUserVerification(Long userId, String verificationUrl) {
        UserVerification userVerification = new UserVerification();
        userVerification.setUserId(userId);
        userVerification.setUrl(verificationUrl);

        UserVerification save = userVerificationEntityService.save(userVerification);
    }
}
