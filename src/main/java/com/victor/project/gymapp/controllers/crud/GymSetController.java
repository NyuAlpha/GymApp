package com.victor.project.gymapp.controllers.crud;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.victor.project.gymapp.dto.GymSetDto;
import com.victor.project.gymapp.services.IGymSetService;

import jakarta.validation.Valid;

/*
 * Este controlador gestiona todas las rutas principales de la aplicación, ya sea el crud, el analisis de datos, etc
 */
@Controller
@RequestMapping(path = "/app")
public class GymSetController {

    private IGymSetService gymSetService;

    public GymSetController(IGymSetService gymSetService) {
        this.gymSetService = gymSetService;
    }

    @PostMapping("gymSet/create")
    public String createGymSet(@ModelAttribute("gymSet") @Valid GymSetDto setDto, BindingResult result,
            RedirectAttributes flash) {

        if (result.hasErrors()) {
            flash.addFlashAttribute("org.springframework.validation.BindingResult.gymSet", result);
            flash.addFlashAttribute("gymSet", setDto);
            return "redirect:/app/exercise/" + setDto.getExerciseId();
        }

        gymSetService.saveGymSet(setDto);
        return "redirect:/app/exercise/" + setDto.getExerciseId();// Redirecciona nuevamente a la vista del ejercicio
    };
}
