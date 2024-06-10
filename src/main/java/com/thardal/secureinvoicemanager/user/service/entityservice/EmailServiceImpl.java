package com.thardal.secureinvoicemanager.user.service.entityservice;

import com.thardal.secureinvoicemanager.user.enums.UserErrorMessages;
import com.thardal.secureinvoicemanager.user.enums.VerificationType;
import com.thardal.secureinvoicemanager.user.exception.ApiException;
import com.thardal.secureinvoicemanager.user.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String firstName, String email, String verificationUrl, VerificationType verificationType) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setText(getEmailMessage(firstName, verificationUrl, verificationType));
            mailMessage.setSubject(String.format("Secure Invoice Manager - %s Verification Email", StringUtils.capitalize(verificationType.getType())));
            mailSender.send(mailMessage);
        } catch (Exception e) {
            log.error("Error sending email to: " + email, e);
        }
    }

    private String getEmailMessage(String firstName, String verificationUrl, VerificationType verificationType) {
        switch (verificationType) {
            case PASSWORD -> {
                return "Hello " + firstName + ",\n\n" +
                        "Please click the link below to verify your email address:\n" +
                        verificationUrl + "\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "Thank you,\n" +
                        "Secure Invoice Manager Team";
            }
            case ACCOUNT -> {
                return "Hello " + firstName + ",\n\n" +
                        "Please click the link below to verify your email address:\n" +
                        verificationUrl + "\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "Thank you,\n" +
                        "Secure Invoice Manager Team";
            }
            default -> throw new ApiException(UserErrorMessages.INCORRECT_EMAIL);
        }

    }
}

