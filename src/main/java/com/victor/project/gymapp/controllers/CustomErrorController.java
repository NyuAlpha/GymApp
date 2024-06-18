package com.victor.project.gymapp.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

/*
 * Este controlador es quien centraliza la gestión de los errores de la aplicación
 */
@Controller
public class CustomErrorController implements ErrorController {


    //Cuando se llama a este endpoint tras un error,  se verifica el error que lo causó y en función
    //de ello devolerá una vista u otra para informar.
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response) {

        //Obtiene el estado del error
        int statusCode = response.getStatus();

        //Redirigue a una vista u otra en función del estado
        if (statusCode == HttpStatus.FORBIDDEN.value()) {
            return "errors/403";
        } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
            return "errors/404";
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return "errors/error";
        }

        return "errors/error";
    }

}
