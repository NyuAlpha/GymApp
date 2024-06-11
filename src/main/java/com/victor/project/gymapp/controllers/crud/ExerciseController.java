package com.victor.project.gymapp.controllers.crud;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.victor.project.gymapp.dto.ExerciseDto;
import com.victor.project.gymapp.dto.GymSetDto;
import com.victor.project.gymapp.services.IExerciseService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping(value = "/app/season/training/exercise")
@AllArgsConstructor
public class ExerciseController {



    private IExerciseService exerciseService;



    

    @PostMapping("/create")
    public String createExercise(@ModelAttribute("exerciseForm") @Valid ExerciseDto exerciseDto, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.exerciseForm", result);
            redirectAttributes.addFlashAttribute("exerciseForm", exerciseDto);
            return "redirect:/app/season/training/" + exerciseDto.getTrainingId() + "/show";
        }

        Integer exerciseId =  exerciseService.saveExercise(exerciseDto).getId();
        redirectAttributes.addFlashAttribute("successExercise", "Ejercicio creado con exito");
        // Redirecciona nuevamente a la vista del entrenamiento
        return "redirect:/app/season/training/exercise/" + exerciseId + "/show";
    };







    //Muestra la vista de un ejercicio concreto junto a sus series vinculadas
    @GetMapping("/{exerciseId}/show")
    public String showExercise(@PathVariable Integer exerciseId, Model model) {

        // Buscamos el ejercicio por id y obtenemos su dto
        ExerciseDto exerciseDto = exerciseService.getFullExerciseById(exerciseId).getDto();
        //Carga el ejercicio de la base de datos para mostrarlo
        model.addAttribute("exerciseDB", exerciseDto);


        //Le pasa un ejercicio al formulario solo si no existe (debido a error de validación)
        if(!model.containsAttribute("exerciseForm")){
            //Si todo está correcto exerciseDB y exerciseForm son identicos
            model.addAttribute("exerciseForm", exerciseDto);
        }
        
        //GymSetDto vacio para el formulario
        model.addAttribute("gymSetVoid", new GymSetDto(exerciseId));
        return "exercises/exercise";
    }






    //Actualiza el ejercicio y redirigue a la vista del ejercicio, si hay errores los envia.
    @PutMapping("/update")
    public String updateExercise(Model model, @ModelAttribute("exerciseForm") @Valid ExerciseDto exerciseDto, BindingResult result,
                                    RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.exerciseForm", result);
            redirectAttributes.addFlashAttribute("exerciseForm", exerciseDto);
            return "redirect:/app/season/training/exercise/" + exerciseDto.getId() + "/show";

        }
        exerciseService.updateExercise(exerciseDto);
        redirectAttributes.addFlashAttribute("successExercise", "Ejercicio actualizado con exito");
        return "redirect:/app/season/training/exercise/" + exerciseDto.getId() + "/show";// Redirecciona nuevamente a la vista del ejercicio
    }






    @DeleteMapping("/delete")
    public String deleteExercise(@RequestParam Integer exerciseId, @RequestParam Integer trainingId) {
        System.out.println("*******************\n\n\n");
        System.out.println("*******************\n\n\n");
        System.out.println("*******************\n\n\n");
        exerciseService.deleteExercise(exerciseId);
        return "redirect:/app/season/training/" + trainingId + "/show";
    }



    @PostMapping("/up")
    public String upExercise(@RequestParam Integer exerciseId, @RequestParam Integer trainingId) {
        exerciseService.up(exerciseId,trainingId);
        return "redirect:/app/season/training/" + trainingId + "/show";
    }

    
    @PostMapping("/down")
    public String downExercise(@RequestParam Integer exerciseId, @RequestParam Integer trainingId) {
        exerciseService.down(exerciseId,trainingId);
        return "redirect:/app/season/training/" + trainingId + "/show";
    }
}
