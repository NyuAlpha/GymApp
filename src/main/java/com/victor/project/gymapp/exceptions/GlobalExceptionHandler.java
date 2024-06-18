package com.victor.project.gymapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import jakarta.servlet.http.HttpServletResponse;

/*
 * Este controlador es un control de excepciones centralizado y personalizado
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //Excepción de Acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex) {
        //Retorna la vista personalizada
        return "/errors/403";
    }





    //Excepción para cuando no existe una url o algún parámetro esté mal
    @ExceptionHandler({NoResourceFoundException.class, MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public String notFound(Exception ex) {
        
        //Retorna la vista personalizada
        return "/errors/404";
    }

    




    //Captura toda excepción no conocida y la personaliza
    @ExceptionHandler(Exception.class)
    public String throwException(Exception e, HttpServletResponse response, Model model){

        // Determinar el código de estado
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        // Establecer el código de estado en la respuesta
        response.setStatus(statusCode);
        //Transfiere los datos de la excepción a la vista
        model.addAttribute("status", statusCode);
        model.addAttribute("exceptionName", e.getClass().getName());
        model.addAttribute("message", e.getMessage());
        model.addAttribute("stackTrace", e.getStackTrace());

        //vista generica para errores no especificados.
        return "/errors/error";
    }


}
