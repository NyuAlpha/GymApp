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
import com.victor.project.gymapp.dto.GymSetDto;
import com.victor.project.gymapp.models.GymSet;
import com.victor.project.gymapp.services.IExerciseService;
import com.victor.project.gymapp.services.IGymSetService;
import com.victor.project.gymapp.services.IUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;







/*
 * Este controlador maneja las rutas con las que se manipula y muestra un ejercicio
 * para un usuario concreto
 */
@Controller
@RequestMapping(value = "/app/season/training/exercise")
@AllArgsConstructor
public class ExerciseController {




    //Los servicios requeridos
    private IExerciseService exerciseService;
    private IUserService userService;
    private IGymSetService gymSetService;


    




    /*
     * Crea un ejercicio nuevo recogiendo los datos enviados por un formulario post
     */
    @PostMapping("/create")
    public String createExercise(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForTraining(exerciseDto.getTrainingId()))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");


        //Validación y redirección en caso de error
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.exerciseCreate", result);
            redirectAttributes.addFlashAttribute("exerciseCreate", exerciseDto);
            return "redirect:/app/season/training/" + exerciseDto.getTrainingId() + "/show";
        }

        
        //Se guarda y se obtiene el id para redireccionar
        Integer exerciseId =  exerciseService.saveExercise(exerciseDto).getId();
        redirectAttributes.addFlashAttribute("successExercise", "Ejercicio creado con exito");
        // Redirecciona nuevamente a la vista del entrenamiento
        return "redirect:/app/season/training/exercise/" + exerciseId + "/show";
    };












    /*
     * Muestra la vista de un ejercicio concreto junto a sus series vinculadas y
     * un formulario para crear series y editar las existentes
     */
    @GetMapping("/{exerciseId}/show")
    public String showExercise(@PathVariable Integer exerciseId, Model model) {


        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForExercise(exerciseId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción"); 


        // Buscamos el ejercicio por id y obtenemos su dto, luego lo pasa a la vista
        System.out.println(exerciseId);
        ExerciseDto exerciseDto = exerciseService.getExerciseById(exerciseId).getDto();
        model.addAttribute("exercise", exerciseDto);

        
        //Le pasa un ejercicio al formulario solo si no existe (debido a error de validación)
        if(!model.containsAttribute("exerciseCreate")){
            model.addAttribute("exerciseCreate", exerciseDto);
        }
        
        //GymSetDto vacio para el formulario de series si no hubo error de validación previo
        if(!model.containsAttribute("gymSetForm")){
            //Si no existe retorna uno vacio, si existe retorna uno identico
            GymSet lastGymSet = gymSetService.getLastGymSet(exerciseId);
            GymSetDto gymSetDto = new GymSetDto(exerciseId); //Esto evita que se le pase un gymSet vacio sin ejercicio
            //Si existe en la base de datos, reemplaza el dto
            if(lastGymSet != null){
                //Se copia a dto y se le asigna el ejercicio actual
                gymSetDto = lastGymSet.getDto();
            }else{
                gymSetDto = new GymSetDto(exerciseId);
            }
            //Se envia al modelo ya sea vacio o con datos si existia una última serie
            model.addAttribute("gymSetForm", gymSetDto);
            
        }

        //Devuelve la vista
        return "exercises/exercise";
    }












    /*
     * Muestra la vista de un ejercicio concreto para editarlo
     */
    @GetMapping("/{exerciseId}/update")
    public String showUpdateExercise(@PathVariable Integer exerciseId, Model model) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForExercise(exerciseId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        // Buscamos el ejercicio por id y obtenemos su dto, luego lo envia a la vista
        ExerciseDto exerciseDto = exerciseService.getExerciseById(exerciseId).getDto();
        model.addAttribute("exercise", exerciseDto);

        //Devuelve la vista
        return "exercises/exercise_update";
    }













    /*
    * Actualiza el ejercicio y redirigue a la vista del ejercicio, si hay errores los envia.
    */
    @PutMapping("/update")
    public String updateExercise(Model model, @ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, BindingResult result,
                                    RedirectAttributes redirectAttributes) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForExercise(exerciseDto.getId()))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");


        //Validación y redirección en caso de error
        if (result.hasErrors()) {
            return "exercises/exercise_update";
        }


        //Se actualiza en la base de datos y redirige a la vista del ejercicio
        exerciseService.updateExercise(exerciseDto);
        redirectAttributes.addFlashAttribute("successExercise", "Ejercicio actualizado con exito");
        return "redirect:/app/season/training/exercise/" + exerciseDto.getId() + "/show";// Redirecciona nuevamente a la vista del ejercicio
    }















    /*
     * Borra el ejercicio  y redirecciona a su entrenamiento padre
     */
    @DeleteMapping("/delete")
    public String deleteExercise(@RequestParam Integer exerciseId, @RequestParam Integer trainingId) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        // Se debe validar tambien el entrenamiento padre para evitar redirigir a entrenamientos ajenos
        if (!userService.checkUserForExercise(exerciseId) || !userService.checkUserForTraining(trainingId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        //Borra y redirecciona
        exerciseService.deleteExercise(exerciseId);
        return "redirect:/app/season/training/" + trainingId + "/show";
    }










    /*
     * Asciende una posición el ejercicio enviado
     */
    @PostMapping("/up")
    public String upExercise(@RequestParam Integer exerciseId, @RequestParam Integer trainingId) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        // Se debe validar tambien el entrenamiento padre para evitar redirigir a entrenamientos ajenos
        if (!userService.checkUserForExercise(exerciseId) || !userService.checkUserForTraining(trainingId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        //Ascenso y redirección
        exerciseService.up(exerciseId,trainingId);
        return "redirect:/app/season/training/" + trainingId + "/show";
    }

    








    /*
     * Desciende una posición el ejercicio enviado
     */
    @PostMapping("/down")
    public String downExercise(@RequestParam Integer exerciseId, @RequestParam Integer trainingId) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        // Se debe validar tambien el entrenamiento padre para evitar redirigir a entrenamientos ajenos
        if (!userService.checkUserForExercise(exerciseId) || !userService.checkUserForTraining(trainingId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        //Descenso y redirección
        exerciseService.down(exerciseId,trainingId);
        return "redirect:/app/season/training/" + trainingId + "/show";
    }










    /*
     * Cancelado de momento
     */
    // @ResponseBody
    // @PostMapping("/name")
    // public List<String> getExerciseName(@RequestParam("token") String token){
    //     return exerciseService.getExerciseNames(token);
    // }
}
