package dto;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.GymSet;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * Clase dto para llevar y traer los datos de un ejercicio a las vistas.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class ExerciseDto {

    private Long id;

    private Long trainingId;

    @NotBlank
    private String exerciseName;//Nombre del ejercicio

    private String variant;//Variante del ejercicio

    private String exerciseComment;//comentario asociado a un ejercicio

    private Set<GymSetDto> gymSetsDtos; //Las series Dto de cada ejercicio irán incorporadas en esta lista

    public static ExerciseDto getsimpleDto(Exercise exercise){
        ExerciseDto exerciseDto = new ExerciseDto();
        exerciseDto.setId(exercise.getId());
        //No comprobamos si tiene nombre de ejercicio puesto que no puede ser null bajo ningún concepto
        exerciseDto.setExerciseName(exercise.getExerciseName().getName());
        exerciseDto.setVariant(exercise.getVariant());
        //Comprobamos si hay un comentario asociado al ejercicio
        if(exercise.getExerciseComment() != null){
             exerciseDto.setExerciseComment(exercise.getExerciseComment().getComment());
         }
        return exerciseDto;
    }

    public static ExerciseDto getDetailsDto(Exercise exercise){
        ExerciseDto exerciseDto = getsimpleDto(exercise);

        //Conseguimos el id de su entrenamiento para tener la referencia al entrenamiento en caso de querer volver atrás en la vista.
        exerciseDto.setTrainingId(exercise.getTraining().getId());

        //Ahora cargamos las series de este ejercicio en caso de que existan
        Set<GymSet> gymSets = exercise.getGymSets();
        if(!gymSets.isEmpty()){
            //Se transforma la lista de series a la lista de series dto
            Set<GymSetDto> setsDto = gymSets.stream()
                .map(gymSet-> GymSetDto.getsimpleDto(gymSet))
                .sorted(Comparator.comparing(GymSetDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
                
            //Finalmente se lo pasamos a la instacia de ejercicio dto
            exerciseDto.setGymSetsDtos(setsDto);
        }

        return exerciseDto;
    }











    //Formatea la series a un formato más compacto
    public String printGymSetFormat(){
        StringBuilder stringBuilder = new StringBuilder(exerciseName)
            .append(" : ")
            .append((variant==null || variant.isBlank())? "":variant)
            .append("-->");

        //Si hay series se procede a crear un formato
        if (gymSetsDtos != null) {

            int setCounter = 0; // Contador de series, contabiliza series consecutivas para un peso ej:60kg(3x8)
            String previousWeight = null; // Peso de la serie anterior, se inicia en un valor imposible para evitar .
            String previousRepetitions = null;
            String actualWeight = null;
            String actualRepetitions = null;

            for (GymSetDto s : gymSetsDtos) {


                //Se inicia pesos y repeticiones, los pesos nulos o 0 serán 0. Además se formatean los decimales

                if(s.getWeight() == null || s.getWeight().equals(0f)){
                    actualWeight = "0";
                }
                else{
                    float floatWeight = s.getWeight().floatValue();
                    int weight = (int)floatWeight;
                    if(floatWeight == weight){
                        actualWeight = String.valueOf(weight);
                    }else{
                        actualWeight = String.valueOf(floatWeight);
                    }
                }
                actualRepetitions = s.getRepetitions().toString();


                //Ahora se empieza a analizar el contenido y a concatenar

                //en la primera serie se inicializa todo
                if(setCounter == 0){
                    stringBuilder.append(actualWeight + "Kg(");
                    setCounter = 1;
                }
                //Se salta la primera serie al no haber peso previo
                else{
                    // Si es el mismo peso no se anota nada.
                    if (previousWeight.equals(actualWeight)){
                        //Si las repeticiones son las mismas sigue contabilizando series identicas
                        if (previousRepetitions.equals(s.getRepetitions())){
                            setCounter++;
                        }
                        //Si son distintas se anotan el resultado de la series previas (series x repeticiones) y se resetea
                        else{
                            stringBuilder.append((setCounter == 1)? "" : setCounter+"x")
                                .append(s.getRepetitions())
                                .append(",");//Añade coma para separar las siguientes con el mismo peso
                            setCounter = 1;
                        }
                    }
                    //Cuando hay cambio de peso
                    else {
                        //Se anotan las series anteriores
                        stringBuilder.append((setCounter == 1)? "" : setCounter+"x")
                                .append(previousRepetitions)
                                .append("), ")//Cierra y añade una coma para otro peso
                                .append(actualWeight)//Se añade el nuevo peso
                                .append("Kg(");
                            setCounter = 1;
                    }
                }
                previousWeight = actualWeight;//Se actualiza el peso previo para la serie siguiente.
                previousRepetitions = actualRepetitions;//Se actualiza las repetiones previas para la serie siguiente.
            }

            //Una vez finalizado se anota la última serie.
            stringBuilder.append((setCounter == 1)? "" : setCounter+"x")
            .append(previousRepetitions)
            .append(")");//Fin de todas las series.

        }

        else{
            stringBuilder.append("Sin series");
        }
        return stringBuilder.toString();
    }

}
