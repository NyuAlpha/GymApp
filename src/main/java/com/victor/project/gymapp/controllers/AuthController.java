package com.victor.project.gymapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.victor.project.gymapp.services.AuthService;

import dto.UserDto;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @GetMapping("/login")
    public String getLogin(Model model){

        model.addAttribute("userDto", new UserDto());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid UserDto userDto, BindingResult result, Model model){

        if(result.hasErrors()){
            return "auth/login";
        }

        if(authService.exitsUsernamePassword(userDto.getUsername(), userDto.getPassword())){
            return "redirect:app/crud";
        }

        model.addAttribute("invalidError", "Nombre de usuario o contraseña incorrecto");
        return "auth/login";

        
    }

}
