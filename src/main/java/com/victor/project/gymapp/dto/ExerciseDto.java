package com.victor.project.gymapp.dto;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.victor.project.gymapp.models.Exercise;
import com.victor.project.gymapp.models.GymSet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    private Integer id;

    private Integer trainingId;

    @NotBlank
    @Size(max=20, message = "No debe contener más de 20 caracteres")
    private String exerciseName;// Nombre del ejercicio

    @Size(max=20, message = "No debe contener más de 20 caracteres")
    private String variant;// Variante del ejercicio

    @Size(max = 100, message = "No debe de tener más de 100 caracteres")
    private String exerciseComment;// comentario asociado a un ejercicio

    private Byte exerciseOrder;//Orden del ejercicio, es obligatorio, pero no se recupera del formulario

    private Set<GymSetDto> gymSetsDtos; // Las series Dto de cada ejercicio irán incorporadas en esta lista



    public ExerciseDto(Integer trainingId) {
        this.trainingId = trainingId;
    }



    // Formatea la series a un formato más compacto
    public String printGymSetFormat() {
        
        StringBuilder stringBuilder = new StringBuilder();

        // Si hay series se procede a crear un formato
        if (gymSetsDtos != null) {

            int setCounter = 0; // Contador de series, contabiliza series consecutivas para un peso ej:60kg(3x8)
            String previousWeight = null; // Peso de la serie anterior, se inicia en un valor imposible para evitar .
            String previousRepetitions = null;
            String actualWeight = null;
            String actualRepetitions = null;

            for (GymSetDto s : gymSetsDtos) {

                // Se inicia pesos y repeticiones, los pesos nulos o 0 serán 0. Además se
                // formatean los decimales

                if (s.getWeight() == null || s.getWeight().equals(0f)) {
                    actualWeight = "0";
                } else {
                    float floatWeight = s.getWeight().floatValue();
                    int weight = (int) floatWeight;
                    if (floatWeight == weight) {
                        actualWeight = String.valueOf(weight);
                    } else {
                        actualWeight = String.valueOf(floatWeight);
                    }
                }
                actualRepetitions = s.getRepetitions().toString();

                // Ahora se empieza a analizar el contenido y a concatenar

                // en la primera serie se inicializa todo
                if (setCounter == 0) {
                    stringBuilder.append(actualWeight + "Kg(");
                    setCounter = s.getTimesRepeated();
                }
                // Se salta la primera serie al no haber peso previo
                else {
                    // Si es el mismo peso no se anota nada.
                    if (previousWeight.equals(actualWeight)) {
                        // Si las repeticiones son las mismas sigue contabilizando series identicas
                        if (previousRepetitions.equals(s.getRepetitions().toString())) {
                            setCounter += s.getTimesRepeated();
                        }
                        // Si son distintas se anotan el resultado de la series previas (series x
                        // repeticiones) y se resetea
                        else {
                            stringBuilder.append((setCounter == 1) ? "" : setCounter + "x")
                                    .append(s.getRepetitions())
                                    .append(",");// Añade coma para separar las siguientes con el mismo peso
                            setCounter = 1;
                        }
                    }
                    // Cuando hay cambio de peso
                    else {
                        // Se anotan las series anteriores
                        stringBuilder.append((setCounter == 1) ? "" : setCounter + "x")
                                .append(previousRepetitions)
                                .append("), ")// Cierra y añade una coma para otro peso
                                .append(actualWeight)// Se añade el nuevo peso
                                .append("Kg(");
                        setCounter = s.getTimesRepeated();
                    }
                }
                previousWeight = actualWeight;// Se actualiza el peso previo para la serie siguiente.
                previousRepetitions = actualRepetitions;// Se actualiza las repetiones previas para la serie siguiente.
            }

            // Una vez finalizado se anota la última serie.
            stringBuilder.append((setCounter == 1) ? "" : setCounter + "x")
                    .append(previousRepetitions)
                    .append(")");// Fin de todas las series.

        }
        //Si el set de series está vacio
        else {
            stringBuilder.append("Sin series");
        }
        return stringBuilder.toString();
     }


    public String printShortComment(){
        if(exerciseComment.length() > 10)
            return exerciseComment.substring(0, 10).concat("...");

        return exerciseComment;
    }
}
