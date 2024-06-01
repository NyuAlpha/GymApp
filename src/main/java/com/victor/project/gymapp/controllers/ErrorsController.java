package com.victor.project.gymapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorsController {

    @GetMapping("/error_403")
    public String getError403() {
        return "errors/error_403";
    }

}
