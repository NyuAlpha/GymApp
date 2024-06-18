package com.victor.project.gymapp.controllers.crud;


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






/*
 * Este controlador maneja las rutas con las que se manipula y muestra cada entrenamiento 
 * para un usuario concreto
 */
@Controller
@AllArgsConstructor
@RequestMapping(path = { "/app/season/training" })
public class TrainingController {


    //Los servicios requeridos
    private ITrainingService trainingService;
    private IUserService userService;












    /*
     * Muestra el entrenamiento pasado en el id junto a sus ejercicios y un formulario para crear ejercicios
     */
    @GetMapping("{trainingId}/show")
    public String showTraining(Model model, @PathVariable("trainingId") Integer trainingId) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForTraining(trainingId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        // Cargamos el entrenamiento
        TrainingDto trainingDto = trainingService.getTrainingById(trainingId).getFullDto();
        //Ahora se mandan todos los datos y sus detalles a la vista
        model.addAttribute("training", trainingDto);
        // Se envian los ejercicios a la vista de forma separada
        model.addAttribute("exerciseDtos", trainingDto.getExerciseDtos());

        //envia un ejercicio vacio para el formulario, solo si no existe uno previo
        if (!model.containsAttribute("exercise")) {
            model.addAttribute("exercise", new ExerciseDto(trainingId));
        }

        return "trainings/training";
    }


    

    // Crea y guarda un entrenamiento y redirecciona a la vista del entrenamiento
    @PostMapping(path = "/create")
    public String processTraining(Model model, @ModelAttribute("training") @Valid TrainingDto trainingDto,
                            BindingResult result, RedirectAttributes flash) {
        
        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForSeasonId(trainingDto.getSeasonId()))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");
                                
        //Valida y devuelve el error a la vista si falla
        if (result.hasErrors()){
            flash.addFlashAttribute("org.springframework.validation.BindingResult.training", result);
            flash.addFlashAttribute("training", trainingDto);
            return "redirect:/app/season/"+ trainingDto.getSeasonId() +"/show";
        }


        // Guardamos y conseguimos el id para redireccionar al entrenamiento
        Integer trainingId =  trainingService.saveTraining(trainingDto).getId();
        flash.addFlashAttribute("success", "Entrenamiento creado con exito");
        return "redirect:/app/season/training/"+ trainingId +"/show";
    }











    

    /*
     * Devuelve la vista de edición del entrenamiento para actualizarlo, envia un formulario
     */
    @GetMapping("{trainingId}/update")
    public String editTraining(Model model, @PathVariable("trainingId") Integer trainingId) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForTraining(trainingId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción"); 

        // Cargamos el entrenamiento si existe, si no existe se tratará el error
        TrainingDto trainingDto = trainingService.getTrainingById(trainingId).getFullDto();
        // Ahora se mandan todos los datos y sus detalles a la vista
        model.addAttribute("training", trainingDto);

        return "trainings/training_update";
    }









    /*
     * Actualiza el entrenamiento utilizando los datos del formulario enviados y redirige al entrenamiento
     */
    @PutMapping(value = "/update")
    public String updateTraining(Model model,@ModelAttribute("training") @Valid TrainingDto trainingDto,
                                BindingResult result, RedirectAttributes flash) {

        
        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForTraining(trainingDto.getId()))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción"); 

        //Se validan los datos y si hay error se muestra en la vista
        if (result.hasErrors()) {
            return "trainings/training_update";
        }

        // Guardamos y conseguimos el id para redireccionar a el
        trainingService.updateTraining(trainingDto);
        flash.addFlashAttribute("success", "Entrenamiento guardado con exito");

        return "redirect:/app/season/training/" + trainingDto.getId() +"/show";
    }










    /*
     * Borra el entrenamiento mediante su id y redirigue a su temporada padre
     */
    @DeleteMapping("/delete")
    public String deleteTraining(@RequestParam("trainingId") Integer trainingId, @RequestParam("seasonId") Integer seasonId,
                                RedirectAttributes flash) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        // Se debe validar tambien la temporada padre para evitar redirigir a temporadas ajenas
        if (!userService.checkUserForTraining(trainingId) || !userService.checkUserForSeasonId(seasonId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");
        
        //Se borra el entrenamiento y se redirigue a su temporada padre
        trainingService.deleteTraining(trainingId);
        flash.addFlashAttribute("successTraining", "Entrenamiento eliminado con exito");
        return "redirect:/app/season/" + seasonId + "/show";
    }



}
