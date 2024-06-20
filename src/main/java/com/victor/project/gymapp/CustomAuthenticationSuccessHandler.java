package com.victor.project.gymapp;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*
 * Esta clase configura las acciones que se llevan a cabo cuando un usuario se auntentica
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    //Cuando se auntentique se procederá a dirigir a la ruta de temporadas
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.sendRedirect(request.getContextPath() + "/");
    }

}

