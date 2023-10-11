package com.thardal.secureinvoicemanager.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thardal.secureinvoicemanager.base.entity.HttpResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.time.LocalTime.now;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        HttpResponse httpResponse = HttpResponse.error(FORBIDDEN.value(), FORBIDDEN, "You don't have enough permission");

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.value());
        ServletOutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
    }
}
