package com.victor.project.gymapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/control")
    public String getAdmin(){
        return "Solo administradores tienen permisos";
    }

    

}
