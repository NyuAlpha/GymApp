package com.victor.project.gymapp.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dto.UserDto;

@Controller
public class AuthController {


    @GetMapping("/login")
    public String getLogin(Model model, Principal principal, 
                @RequestParam(value="error",required=false) String error,
                RedirectAttributes redirectAttributes){

        //Si un usuario logeado intenta logearse se le devuelve a la vista principal y se le comunica ya tiene una sesión.
        if(principal!=null){
            redirectAttributes.addFlashAttribute("info", "Ya has iniciado sesión");
            return "redirect:/app/training/list";
        }
        if(error != null){
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }

        model.addAttribute("user", new UserDto());
        return "auth/login";
    }

    // @PostMapping("/login")
    // public String login(@Valid UserDto userDto, BindingResult result, Model model){

    //     if(result.hasErrors()){
    //         return "auth/login";
    //     }

    //     if(authService.exitsUsernamePassword(userDto.getUsername(), userDto.getPassword())){
    //         return "redirect:app/crud";
    //     }

    //     model.addAttribute("invalidError", "Nombre de usuario o contraseña incorrecto");
    //     return "auth/login";

        
    // }

}
