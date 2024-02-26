package com.thardal.secureinvoicemanager.user.service;

import com.thardal.secureinvoicemanager.security.provider.TokenProvider;
import com.thardal.secureinvoicemanager.user.dto.UserDto;
import com.thardal.secureinvoicemanager.user.entity.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public AuthService(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public UserDto getAuthenticatedUser(Authentication authentication) {
        return ((UserDto) authentication.getPrincipal());
    }

    public UserDto getLoggedUser(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }

    public Authentication authenticate(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));
        return authentication;
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
