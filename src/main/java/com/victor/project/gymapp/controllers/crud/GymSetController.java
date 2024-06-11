package com.victor.project.gymapp.controllers.crud;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.victor.project.gymapp.dto.GymSetDto;
import com.victor.project.gymapp.services.IGymSetService;

import java.util.List;
import java.util.ArrayList;

import jakarta.validation.Valid;

/*
 * Este controlador gestiona todas las rutas principales de la aplicación, ya sea el crud, el analisis de datos, etc
 */
@Controller
@RequestMapping(path = "/app/season/training/exercise/gymset")
public class GymSetController {

    private IGymSetService gymSetService;

    public GymSetController(IGymSetService gymSetService) {
        this.gymSetService = gymSetService;
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<?> createGymSet(@RequestBody @Valid GymSetDto gymSetDto, BindingResult result,
            RedirectAttributes flash) {

                if (result.hasErrors()) {
                    // Aquí puedes manejar los errores de validación y devolver una respuesta adecuada
                    List<String> errors = new ArrayList<>();
                    result.getFieldErrors().forEach(err-> errors.add(err.getDefaultMessage()));
                    return ResponseEntity.badRequest().body(errors);
                }
        
                gymSetService.saveGymSet(gymSetDto);
                // Aquí puedes devolver un mensaje de éxito o la entidad actualizada
                return ResponseEntity.ok(gymSetDto);

    };


    @ResponseBody
    @PutMapping("/update")
    public ResponseEntity<?> updateGymSet(@RequestBody @Valid GymSetDto gymSetDto, BindingResult result) {

        if (result.hasErrors()) {
            // Aquí puedes manejar los errores de validación y devolver una respuesta adecuada
            List<String> errors = new ArrayList<>();
            result.getFieldErrors().forEach(err-> errors.add(err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        gymSetService.updateGymSet(gymSetDto);
        // Aquí puedes devolver un mensaje de éxito o la entidad actualizada
        return ResponseEntity.ok(gymSetDto);
    }

    @DeleteMapping("/delete")
    public String DeleteGymSet(@RequestParam Integer exerciseId, @RequestParam Byte setOrder) {

        gymSetService.deleteGymSet(exerciseId, setOrder);
        // Aquí puedes devolver un mensaje de éxito o la entidad actualizada
        return "redirect:/app/season/training/exercise/" + exerciseId + "/show";// Redirecciona nuevamente a la vista del ejercicio
    }

    @PostMapping("/up")
    public String upExercise(@RequestParam Integer exerciseId, @RequestParam Byte setOrder) {
        gymSetService.up(exerciseId,setOrder);
        return "redirect:/app/season/training/exercise/" + exerciseId + "/show";
    }

    
    @PostMapping("/down")
    public String downExercise(@RequestParam Integer exerciseId, @RequestParam Byte setOrder) {
        gymSetService.down(exerciseId,setOrder);
        return "redirect:/app/season/training/exercise/" + exerciseId + "/show";
    }

}
