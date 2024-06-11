package com.victor.project.gymapp.controllers.crud;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
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
import com.victor.project.gymapp.dto.TrainingDto;
import com.victor.project.gymapp.services.ITrainingService;
import com.victor.project.gymapp.services.IUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(path = { "/app/season/training" })
public class TrainingController {

    private ITrainingService trainingService;
    private IUserService userService;



    // Devuelve la vista necesaria para crear un entrenamiento mediante post para no enviar la temporada en la url
    @GetMapping(path = "/create/{seasonId}")
    public String createTraining(Model model, @PathVariable("seasonId") Integer seasonId) {

        // Se comprueba si el usuario es el propietario de la temporada
        if (!userService.checkUserForSeasonId(seasonId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        TrainingDto trainingDto = new TrainingDto(LocalDate.now());
        trainingDto.setSeasonId(seasonId);

        model.addAttribute("training", trainingDto);
        return "trainings/training_create";
    }





    

    // Crea y guarda un entrenamiento y redirecciona a la vista del entrenamiento
    @PostMapping(path = "/create")
    public String processTraining(Model model, @ModelAttribute("training") @Valid TrainingDto trainingDto,
                            BindingResult result, RedirectAttributes flash) {
        
        
        if (result.hasErrors())
            return "trainings/training_create";

        // Guardamos y conseguimos el id para redireccionar a el
        Integer trainingId =  trainingService.saveTraining(trainingDto).getId();
        flash.addFlashAttribute("successTraining", "Entrenamiento creado con exito");

        return "redirect:/app/season/training/"+ trainingId +"/show";
    }






    // Para ver y editar los datos de un entrenamiento concreto en la vista, sirve todos los datos, incluidos ejercicios y series.
    @GetMapping("{trainingId}/show")
    public String showTraining(Model model, @PathVariable("trainingId") Integer trainingId) {


        // Cargamos el entrenamiento si existe, si no existe se tratará el error
        TrainingDto trainingDto = trainingService.getFullTrainingById(trainingId).getFullDto();
        // Ahora se mandan todos los datos y sus detalles a la vista
        model.addAttribute("training", trainingDto);
        // Se envian los ejercicios a la vista de forma separada
        model.addAttribute("exerciseDtos", trainingDto.getExerciseDtos());

        //envia un ejercicio vacio para el formulario, solo si no existe uno previo
        if (!model.containsAttribute("exerciseForm")) {
            model.addAttribute("exerciseForm", new ExerciseDto(trainingId));
        }

        return "trainings/training";
    }


    // Para ver y editar los datos de un entrenamiento concreto en la vista, sirve todos los datos, incluidos ejercicios y series.
    @GetMapping("{trainingId}/update")
    public String editTraining(Model model, @PathVariable("trainingId") Integer trainingId) {

        // Cargamos el entrenamiento si existe, si no existe se tratará el error
        TrainingDto trainingDto = trainingService.getFullTrainingById(trainingId).getFullDto();
        // Ahora se mandan todos los datos y sus detalles a la vista
        model.addAttribute("training", trainingDto);

        return "trainings/training_update";
    }

    @PutMapping(value = "/update")
    public String updateTraining(Model model,@ModelAttribute("training") @Valid TrainingDto trainingDto,
                                BindingResult result, RedirectAttributes flash) {

        if (result.hasErrors()) {
            // Asigna a los campos de exercise un dto vacio
            model.addAttribute("exercise", new ExerciseDto());
            model.addAttribute("training", trainingDto);// Lo reenvía al formulario

            // Cargamos los ejercicios
            Set<ExerciseDto> exerciseDtos = trainingService.getFullTrainingById(trainingDto.getId()).getFullDto().getExerciseDtos();
            // Se reenvian los ejercicios a la vista de forma separada
            model.addAttribute("exerciseDtos", exerciseDtos);

            return "trainings/training_update";
        }
        // Guardamos y conseguimos el id para redireccionar a el
        trainingService.updateTraining(trainingDto);
        flash.addFlashAttribute("successTraining", "Entrenamiento guardado con exito");

        return "redirect:/app/season/training/" + trainingDto.getId() +"/show";
    }











    @DeleteMapping("/delete")
    public String deleteTraining(@RequestParam("trainingId") Integer trainingId, @RequestParam("seasonId") Long seasonId,
                                RedirectAttributes flash) {
        trainingService.deleteTraining(trainingId);
        flash.addFlashAttribute("successTraining", "Entrenamiento eliminado con exito");
        return "redirect:/app/season/" + seasonId + "/show";
    }



}
