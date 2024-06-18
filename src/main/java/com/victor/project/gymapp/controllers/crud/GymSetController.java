package com.victor.project.gymapp.controllers.crud;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.victor.project.gymapp.dto.GymSetDto;
import com.victor.project.gymapp.services.IGymSetService;
import com.victor.project.gymapp.services.IUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;






/*
 * Este controlador maneja las rutas con las que se manipula una serie perteneciente a un usuario,
 * no maneja vistas
 */
@Controller
@RequestMapping(path = "/app/season/training/exercise/gymset")
@AllArgsConstructor
public class GymSetController {


    //Los servicios requeridos
    private IGymSetService gymSetService;
    private IUserService userService;





    /*
     * Crea una nueva serie dentro de un ejercicio, recogiendo los datos del formulario enviado
     */
    @PostMapping("/create")
    public String createGymSet(@ModelAttribute("gymSet") @Valid GymSetDto gymSetDto, BindingResult result,
            RedirectAttributes flash) {

        //Validación y redirección en caso de error
        if (result.hasErrors()) {
            flash.addFlashAttribute("org.springframework.validation.BindingResult.gymSetForm", result);
            flash.addFlashAttribute("gymSetForm", gymSetDto);
            return "redirect:/app/season/training/exercise/" + gymSetDto.getExerciseId() + "/show";// Redirecciona nuevamente a la vista del ejercicio
        }

        //Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        //En este caso solo se comprueba el ejercicio, puesto que no tienen id propia.
        if (!userService.checkUserForExercise(gymSetDto.getExerciseId()))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        //Se guarda y se redirecciona al ejercicio padre
        gymSetService.saveGymSet(gymSetDto);
        flash.addFlashAttribute("successGymSet", "Serie creada con exito");
        return "redirect:/app/season/training/exercise/" + gymSetDto.getExerciseId() + "/show";// Redirecciona nuevamente a la vista del ejercicio
    };











    /*
     * Modifica una serie de un ejercicio concreto mediante los datos enviados por formulario
     */
    @PutMapping("/update")
    public String updateGymSet(@ModelAttribute("gymSet") @Valid GymSetDto gymSetDto, BindingResult result,
            RedirectAttributes flash) {

        //Validación y redirección en caso de error
        if (result.hasErrors()) {
            flash.addFlashAttribute("org.springframework.validation.BindingResult.gymSetUpdate", result);
            flash.addFlashAttribute("gymSetUpdate", gymSetDto);
            return "redirect:/app/season/training/exercise/" + gymSetDto.getExerciseId() + "/show";// Redirecciona nuevamente a la vista del ejercicio
        }

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        //En este caso solo se comprueba el ejercicio, puesto que no tienen id propia.
        if (!userService.checkUserForExercise(gymSetDto.getExerciseId()))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        //Se actualiza y se redirecciona al ejercicio padre
        gymSetService.updateGymSet(gymSetDto);
        flash.addFlashAttribute("successGymSet", "Serie actualizada con exito");
        return "redirect:/app/season/training/exercise/" + gymSetDto.getExerciseId() + "/show";// Redirecciona nuevamente a la vista del ejercicio
    }








    /*
     * Elimina una serie en base a su ejercicio y el orden que ocupa dentro de el
     */
    @DeleteMapping("/delete")
    public String DeleteGymSet(@RequestParam Integer exerciseId, @RequestParam Byte setOrder,RedirectAttributes flash) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        //En este caso solo se comprueba el ejercicio, puesto que no tienen id propia.
        if (!userService.checkUserForExercise(exerciseId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        //Se borra y se redirigue a la vista del ejercicio padre
        gymSetService.deleteGymSet(exerciseId, setOrder);
        flash.addFlashAttribute("successGymSet", "Serie eliminada con exito");
        return "redirect:/app/season/training/exercise/" + exerciseId + "/show";// Redirecciona nuevamente a la vista del ejercicio
    }







    /*
     * Asciende la serie de orden una posición dentro de las series del ejercicio
     */
    @PostMapping("/up")
    public String upExercise(@RequestParam Integer exerciseId, @RequestParam Byte setOrder) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        //En este caso solo se comprueba el ejercicio, puesto que no tienen id propia.
        if (!userService.checkUserForExercise(exerciseId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        //La asciende y redirige
        gymSetService.up(exerciseId,setOrder);
        return "redirect:/app/season/training/exercise/" + exerciseId + "/show";
    }







    /*
     * Desciende la serie de orden una posición dentro de las series del ejercicio
     */
    @PostMapping("/down")
    public String downExercise(@RequestParam Integer exerciseId, @RequestParam Byte setOrder) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        //En este caso solo se comprueba el ejercicio, puesto que no tienen id propia.
        if (!userService.checkUserForExercise(exerciseId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        //La desciende y redirige
        gymSetService.down(exerciseId,setOrder);
        return "redirect:/app/season/training/exercise/" + exerciseId + "/show";
    }

}
