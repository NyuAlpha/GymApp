package com.victor.project.gymapp;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // // Obtener el usuario autenticado
        // Object principal = authentication.getPrincipal();
        // String uuid = "";

        // if (principal instanceof CustomUserDetails) {
        //     uuid = ((CustomUserDetails) principal).getUuid();
        // }

        // Redirigir a la URL deseada
        response.sendRedirect(request.getContextPath() + "/app/season/list");
    }

}

