package com.victor.project.gymapp.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.Training;
import com.victor.project.gymapp.services.CrudService;
import com.victor.project.gymapp.util.paginator.PageRender;

import dto.ExerciseDto;
import dto.TrainingDto;
import jakarta.validation.Valid;



/*
 * Este controlador gestiona todas las rutas principales de la aplicación, ya sea el crud, el analisis de datos, etc
 */
@Controller
@RequestMapping(path="/app")
public class CrudController {

    private CrudService crudService;

    public CrudController(CrudService crudService){
        this.crudService = crudService;
    }

    
    @GetMapping(path="/crud")
    public String getCrud(Model model,@RequestParam(name="page",defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, 4);
        Page<Training> trainings = crudService.findAllTrainingsWithComment(pageable);
        Page<TrainingDto> trainingsDto = trainings.map(TrainingDto::getSimpleDto);

        PageRender<TrainingDto> pageRender = new PageRender<>("/app/crud", trainingsDto);
        model.addAttribute("trainings",trainingsDto);
        model.addAttribute("page", pageRender);
        return "crud";
    }


    @GetMapping(path="training/create")
    public String createTraining(Model model){

        model.addAttribute("training", new TrainingDto(LocalDate.now()));
        return "training";
    }

    @PostMapping(path="training/create")
    public String processTraining(Model model, @ModelAttribute("training") @Valid TrainingDto trainingDto , BindingResult result, RedirectAttributes flash){

        if(result.hasErrors()){
            model.addAttribute("training", trainingDto);//Lo reenvia al formulario
            return "training";
        }
        //Guardamos y conseguimos el id para redireccionar a el
        Long trainingId = crudService.saveTraining(trainingDto).getId();
        flash.addFlashAttribute("success", "Entrenamiento creado con exito");
        return "redirect:/app/training/" + trainingId;
    }

    @GetMapping("training/delete/{id}")
    public String deleteTraining(@PathVariable("id") Long id){
        crudService.deleteTraining(id);
        return "redirect:/app/crud";
    }

    //Para ver y editar los datos de un entrenamiento concreto en la vista, sirve todos los datos, incluidos ejercicios y series.
    @GetMapping("training/{id}")
    public String showTraining(@PathVariable("id") Long id, Model model) {
        //Cargamos el entrenamiento si existe, si no existe se tratará el error
        TrainingDto trainingDto = crudService.getFullTrainingById(id);
        //Ahora se mandan todos los datos y sus detalles a la vista
        model.addAttribute("training", trainingDto);

        // Para rellenar el formulario de ejercicio con campos vacios
        model.addAttribute("exercise", new ExerciseDto());

        // Añadir el ID de entrenamiento al modelo para que todo ejercicio lo envie de vuelta en el formulario
        model.addAttribute("trainingId", id);

        return "training_editor";
    }

    @GetMapping("training/update")
    public String updateTraining(Model model, @ModelAttribute("training") @Valid TrainingDto trainingDto , BindingResult result, RedirectAttributes flash){
        

        if(result.hasErrors()){
            //Asigna a los campos de exercise un dto vacio
            model.addAttribute("exercise", new ExerciseDto());
            model.addAttribute("training", trainingDto);//Lo reenvía al formulario
            return "training_editor";
        }
        //Guardamos y conseguimos el id para redireccionar a el
        Long trainingId = crudService.updateTraining(trainingDto).getId();
        flash.addFlashAttribute("success", "Entrenamiento guardado con exito");
        return "redirect:/app/training/" + trainingId;
    }

    //Nuevo update
    @PostMapping("training/update")
    public ResponseEntity<?> updateTraining(@Valid @RequestBody TrainingDto trainingDto, BindingResult result) {
        if (result.hasErrors()) {
            // Devolver errores de validación
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        
        // Guardar la actualización
        TrainingDto updatedTraining = TrainingDto.getDetailsDto(crudService.updateTraining(trainingDto));
        return ResponseEntity.ok(updatedTraining);
    }


    @PostMapping("exercise/create")
    public String createExercise(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, BindingResult result,RedirectAttributes redirectAttributes){

        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.exercise", result);
            redirectAttributes.addFlashAttribute("exercise", exerciseDto);
            return "redirect:/app/training/" + exerciseDto.getTrainingId();
        }

        crudService.saveExercise(exerciseDto);
        return "redirect:/app/training/" + exerciseDto.getTrainingId();//Redirecciona nuevamente a la vista del entrenamiento
    };

    @GetMapping("exercise/{id}")
    @ResponseBody
    public ResponseEntity<Object> getFullExerciseById(@PathVariable Long id) {
        
        Optional<Exercise> optionalTraining =  crudService.getFullExerciseById(id);

        if(optionalTraining.isPresent()){
            return ResponseEntity.ok().body(ExerciseDto.getDetailsDto(optionalTraining.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body("Not found exercise!");
    }

}
