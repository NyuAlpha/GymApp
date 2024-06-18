package com.victor.project.gymapp.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.victor.project.gymapp.dto.UserDto;

/*
 * Este controlador es el encargado de gestionar las acciones de un usuario respecto a su cuenta de usuario
 */
@Controller
public class AuthController {



    //Este endpoint es el que devuelve la vista de login al usuario para que se loguee
    @GetMapping("/login")
    public String getLogin(Model model, Principal principal,@RequestParam(value = "error", required = false) String error,
            RedirectAttributes redirectAttributes) {

        //Si un usuario logeado intenta logearse se le devuelve a la vista principal y se le comunica ya tiene una sesión.
        if (principal != null) {
            redirectAttributes.addFlashAttribute("info", "Ya has iniciado sesión");
            return "redirect:/app/training/list";
        }
        
        //Si hubo un error al hacer login devuelve el mensaje de vuelta al propio login para informar al usuario
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }

        //Devuelve la vista de login con un usuario vacio al formulario.
        model.addAttribute("user", new UserDto());
        return "auth/login";
    }


}
