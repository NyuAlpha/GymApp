package com.victor.project.gymapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Controlador para tareas de administrador 
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    //Este endpoint permitirá en el futuro al administrador gestionar la aplicación.
    @GetMapping("/control")
    public String getAdmin(){
        return "En construcción";
    }


}
