package dto;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.Training;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingDto {

    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate date;

    private String title;

    private String trainingComment;//comentario asociado al entrenamiento, debe cargar siempre

    private Set<ExerciseDto> exerciseDtos; //Los ejercicios asociados

    
    public TrainingDto(LocalDate date, String trainingComment) {
        this.date = date;
        this.trainingComment = trainingComment;
    }

    public TrainingDto(LocalDate date) {
        this.date = date;
    }

    public TrainingDto() {
    }

    //Devuelve un DTO con solo los datos del entrenamiento
    public static TrainingDto getSimpleDto(Training training) {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setId(training.getId());
        trainingDto.setDate(training.getDate());
        trainingDto.setTitle(training.getTitle());
        
        if(training.getTrainingComment() != null){
            trainingDto.setTrainingComment(training.getTrainingComment().getComment());
        }

        return trainingDto;
    }


    //Devuelve un DTO con todos los datos anidados para mostrar en la vista del editor de entrenamientos
    public static TrainingDto getDetailsDto(Training training) {
        TrainingDto trainingDto = getSimpleDto(training);

        //Ahora cargamos los ejercicios de este entrenamiento en caso de que existan
        Set<Exercise> exercises = training.getExercises();
        if(!exercises.isEmpty()){
            //Se transforma la lista de ejercicios a la lista de ejercicios dto ordenada
            Set<ExerciseDto> exerciseDtos = exercises.stream()
                .map(exercise-> ExerciseDto.getDetailsDto(exercise))
                .sorted(Comparator.comparing(ExerciseDto::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
            //Finalmente se lo pasamos a la instacia de entrenamiento dto
            trainingDto.setExerciseDtos(exerciseDtos);
        }

        return trainingDto;
    }

}
