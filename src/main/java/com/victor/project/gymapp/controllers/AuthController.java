package com.victor.project.gymapp.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.victor.project.gymapp.dto.UserDto;
import com.victor.project.gymapp.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/*
 * Este controlador es el encargado de gestionar las acciones de un usuario respecto a su cuenta de usuario
 */
@Controller
@AllArgsConstructor
public class AuthController {

    //Servicio necesario
    private UserService userService;


    //Este endpoint es el que devuelve la vista de login al usuario para que se loguee
    @GetMapping("/login")
    public String getLogin(Model model, Principal principal,@RequestParam(value = "error", required = false) String error,
            RedirectAttributes redirectAttributes) {

        //Si un usuario logeado intenta logearse se le devuelve a la vista principal y se le comunica ya tiene una sesión.
        if (principal != null) {
            redirectAttributes.addFlashAttribute("success", "Ya habias iniciado sesión");
            return "redirect:/app/season/list";
        }
        
        //Si hubo un error al hacer login devuelve el mensaje de vuelta al propio login para informar al usuario
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }

        //Devuelve la vista de login con un usuario vacio al formulario.
        model.addAttribute("user", new UserDto());
        return "auth/login";
    }



    @GetMapping("/sign_up")
    public String getSignUp(Model model, Principal principal,@RequestParam(value = "error", required = false) String error,
    RedirectAttributes redirectAttributes){

        //Si un usuario logeado intenta logearse se le devuelve a la vista principal y se le comunica ya tiene una sesión.
        if (principal != null) {
            redirectAttributes.addFlashAttribute("success", "Ya habias iniciado sesión");
            return "redirect:/app/season/list";
        }

        //Si no hay error de validación previo que pase un user para los campos erroneos, entonces se crea uno vacio.
        if(!model.containsAttribute("user")){
            UserDto userDto = new UserDto();
            model.addAttribute("user", userDto);
        }

        return "auth/sign-up";
    }






    //Se envian los datos del nuevo usuario, se validan y si todo esta correcto se crea el usuario, finalmente se autologea al usuario
    @PostMapping("/sign_up")
    public String getSignUp(@Valid UserDto userDto,BindingResult result, Principal principal,
    RedirectAttributes redirectAttributes, HttpServletRequest request){

        //Si un usuario logeado intenta logearse se le devuelve a la vista principal y se le comunica ya tiene una sesión.
        if (principal != null) {
            redirectAttributes.addFlashAttribute("success", "Ya habias iniciado sesión");
            return "redirect:/app/season/list";
        }

        //Se envian los errores de vuelta a la vista si se producen
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", userDto);
            return "redirect:/sign_up?error";
        }

        //Persistencia en la base de datos
        userService.createUser(userDto);

        try {
            // Autenticación automática después de registrar el usuario
            request.login(userDto.getUsername(), userDto.getPassword());
        } catch (ServletException e) {
            e.printStackTrace(); // Manejar excepción de login aquí
            redirectAttributes.addFlashAttribute("error", "Error al iniciar sesión automáticamente. Por favor, inicia sesión manualmente.");
            return "redirect:/login";
        }

        //Se reenvia a la página por defecto
        redirectAttributes.addFlashAttribute("success", "Registro exitoso, has iniciado sesión automáticamente.");
        return "redirect:/app/season/list"; // Redirigir al home después del registro y login automático
    }


    

}
