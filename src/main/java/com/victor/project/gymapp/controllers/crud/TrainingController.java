package com.victor.project.gymapp.controllers.crud;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.victor.project.gymapp.util.paginator.PageRender;

import com.victor.project.gymapp.models.Training;
import com.victor.project.gymapp.services.ITrainingService;

import dto.ExerciseDto;
import dto.TrainingDto;
import jakarta.validation.Valid;

@Controller
@RequestMapping(path={"/app/training"})
public class TrainingController {


    private ITrainingService trainingService;

    public TrainingController(ITrainingService trainingService){
        this.trainingService = trainingService;
    }
    



    //Muestra la vista con la lista de entrenamiento paginada
    @GetMapping(path="/list")
    public String getTrainingList(Model model,@RequestParam(name="page",defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Training> trainings = trainingService.findAllTrainingsWithComment(pageable);
        Page<TrainingDto> trainingsDto = trainings.map(TrainingDto::getSimpleDto);

        PageRender<TrainingDto> pageRender = new PageRender<>("/app/training/list", trainingsDto);
        model.addAttribute("trainings",trainingsDto);
        model.addAttribute("page", pageRender);
        return "crud";
    }

    



    //Devuelve la vista necesaria para crear un entrenamiento
    @GetMapping(path="/create")
    public String createTraining(Model model){

        model.addAttribute("training", new TrainingDto(LocalDate.now()));
        return "training";
    }




    //Crea y guarda un entrenamiento y redirecciona a la vista del entrenamiento
    @PostMapping(path="/create")
    public String processTraining(Model model, @ModelAttribute("training") @Valid TrainingDto trainingDto , BindingResult result, RedirectAttributes flash){

        if(result.hasErrors()){
            model.addAttribute("training", trainingDto);//Lo reenvia al formulario
            return "training";
        }
        //Guardamos y conseguimos el id para redireccionar a el
        Long trainingId = trainingService.saveTraining(trainingDto).getId();
        flash.addFlashAttribute("success", "Entrenamiento creado con exito");
        return "redirect:/app/training/" + trainingId;
    }








    //Para ver y editar los datos de un entrenamiento concreto en la vista, sirve todos los datos, incluidos ejercicios y series.
    @GetMapping("/{id}")
    public String showTraining(@PathVariable("id") Long id, Model model) {
        //Cargamos el entrenamiento si existe, si no existe se tratará el error
        TrainingDto trainingDto = trainingService.getFullTrainingById(id);
        //Ahora se mandan todos los datos y sus detalles a la vista
        model.addAttribute("training", trainingDto);

        //Se envian los ejercicios a la vista de forma separada
        model.addAttribute("exerciseDtos", trainingDto.getExerciseDtos());

        // Para rellenar el formulario de ejercicio con campos vacios
        model.addAttribute("exercise", new ExerciseDto());

        // Añadir el ID de entrenamiento al modelo para que todo ejercicio lo envie de vuelta en el formulario
        model.addAttribute("trainingId", id);

        return "training_editor";
    }






    @PutMapping(value="/{id}")
    public String updateTraining(Model model,@PathVariable Long id, @ModelAttribute("training") @Valid TrainingDto trainingDto , BindingResult result, RedirectAttributes flash){
        
        

        if(result.hasErrors()){
            //Asigna a los campos de exercise un dto vacio
            model.addAttribute("exercise", new ExerciseDto());
            model.addAttribute("training", trainingDto);//Lo reenvía al formulario

            //Cargamos los ejercicios
            Set<ExerciseDto> exerciseDtos = trainingService.getFullTrainingById(trainingDto.getId()).getExerciseDtos();
            //Se reenvian los ejercicios a la vista de forma separada
            model.addAttribute("exerciseDtos", exerciseDtos);

            return "training_editor";
        }
        //Guardamos y conseguimos el id para redireccionar a el
        trainingService.updateTraining(id,trainingDto);
        flash.addFlashAttribute("success", "Entrenamiento guardado con exito");
        return "redirect:/app/training/" + id;
    }


    

    @GetMapping("/delete/{id}")
    public String deleteTraining(@PathVariable("id") Long id){
        trainingService.deleteTraining(id);
        return "redirect:/app/training/list";
    }
}
