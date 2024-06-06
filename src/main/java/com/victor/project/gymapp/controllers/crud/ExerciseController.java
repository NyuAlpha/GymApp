package com.victor.project.gymapp.controllers.crud;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.victor.project.gymapp.dto.ExerciseDto;
import com.victor.project.gymapp.dto.GymSetDto;
import com.victor.project.gymapp.services.IExerciseService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/app/exercise")
public class ExerciseController {

    private IExerciseService exerciseService;

    public ExerciseController(IExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping("/create")
    public String createExercise(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.exercise", result);
            redirectAttributes.addFlashAttribute("exercise", exerciseDto);
            return "redirect:/app/training/" + exerciseDto.getTrainingId();
        }

        exerciseService.saveExercise(exerciseDto);
        return "redirect:/app/training/" + exerciseDto.getTrainingId();// Redirecciona nuevamente a la vista del
                                                                       // entrenamiento
    };

    @GetMapping("/{id}")
    public String showExercise(@PathVariable Long id, Model model) {

        // Buscamos el ejercicio por id y obtenemos su dto
        ExerciseDto exerciseDto = exerciseService.getFullExerciseById(id);
        System.out.println(exerciseDto);
        // Le pasa el entrenamiento a la vista
        model.addAttribute("exercise", exerciseDto);
        // Le pasa un set vacio a la vista con el id del entrenamiento
        model.addAttribute("gymSet", new GymSetDto(exerciseDto.getId()));
        return "exercise_editor";

    }

    @PostMapping("/update")
    public String updateExercise(Model model, @ModelAttribute("exercise") @Valid ExerciseDto exerciseDto,
            BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("gymSet", new GymSetDto(exerciseDto.getId()));
            model.addAttribute("exercise", exerciseDto);
            return "exercise_editor";
        }
        Long id = exerciseService.updateExercise(exerciseDto).getId();
        return "redirect:/app/exercise/" + id;// Redirecciona nuevamente a la vista del ejercicio
    };

    @GetMapping("/delete/{trainingId}/{id}")
    public String deleteExercise(@PathVariable Long id, @PathVariable Long trainingId) {
        exerciseService.deleteExercise(id);
        return "redirect:/app/training/" + trainingId;
    }
}
