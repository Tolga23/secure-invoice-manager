package com.thardal.secureinvoicemanager.user.service;

import com.thardal.secureinvoicemanager.event.entity.NewUserEvent;
import com.thardal.secureinvoicemanager.event.enums.EventType;
import com.thardal.secureinvoicemanager.security.provider.TokenProvider;
import com.thardal.secureinvoicemanager.user.dto.UserDto;
import com.thardal.secureinvoicemanager.user.entity.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final ApplicationEventPublisher publisher;

    public AuthService(AuthenticationManager authenticationManager, TokenProvider tokenProvider, ApplicationEventPublisher publisher) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.publisher = publisher;
    }

    public UserDto getAuthenticatedUser(Authentication authentication) {
        return ((UserDto) authentication.getPrincipal());
    }

    public UserDto getLoggedUser(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }

    public Authentication authenticate(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));
            return authentication;
        } catch (AuthenticationException e) {
            publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_FAILURE));
            throw new AuthenticationServiceException("Failed to authenticate user", e);
        }
    }

    public String createAccessToken(UserPrincipal userPrincipal) {
        return tokenProvider.createAccessToken(userPrincipal);
    }

    public String createRefreshToken(UserPrincipal userPrincipal) {
        return tokenProvider.createRefreshToken(userPrincipal);
    }

    public Long getSubject(String token, HttpServletRequest request) {
        return tokenProvider.getSubject(token, request);
    }

    public boolean isTokenValid(Long userId, String token) {
        return tokenProvider.isTokenValid(userId, token);
    }

}
