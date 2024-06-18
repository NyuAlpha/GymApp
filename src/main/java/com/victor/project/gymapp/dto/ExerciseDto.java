package com.victor.project.gymapp.dto;

import java.util.Set;
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

    //Id propio e id de su entrenamiento padre
    private Integer id;
    private Integer trainingId;

    @NotBlank(message = "El nombre del ejercicio es un campo obligatorio")
    @Size(max=30, message = "El nombre del ejercicio no debe contener más de 30 caracteres")
    private String exerciseName;// Nombre del ejercicio

    @Size(max=30, message = "La variante no debe contener más de 30 caracteres")
    private String variant;// Variante del ejercicio

    @Size(max = 255, message = "El comentario no debe contener más de 100 caracteres")
    private String exerciseComment;// comentario asociado a un ejercicio

    private Byte exerciseOrder;//Orden del ejercicio, es obligatorio, pero no se recupera del formulario

    private Set<GymSetDto> gymSetsDtos; // Las series Dto de cada ejercicio irán incorporadas en esta lista







    
    /*
     * Crea un exercise DTO con un id de entrenamiento padre
     */
    public ExerciseDto(Integer trainingId) {
        this.trainingId = trainingId;
    }



    /*
     * Formatea la series a un formato más compacto para que ocupen poco espacio y se muestren en la vista
     */
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

                //PESOS
                // Se inicia pesos y repeticiones, los pesos nulos o 0 serán 0. Además se formatean los decimales
                //Si es peso es nulo o 0 se mostrara como 0kg
                if (s.getWeight() == null || s.getWeight().equals(0f)) {
                    actualWeight = "0";
                }
                //Si no es 0 o nulo se procede a convertir
                else {
                    float floatWeight = s.getWeight().floatValue();//Conseguimos el peso actual
                    int weight = (int) floatWeight;//Obtenemos su parte entera
                    if (floatWeight == weight) {//Si es igual que la parte entera nos quedamos con dicha parte
                        actualWeight = String.valueOf(weight);
                    } else {
                        actualWeight = String.valueOf(floatWeight);
                    }
                }

                //REPETICIONES
                actualRepetitions = s.getRepetitions().toString();



                //******************************************************************************** */
                // Ahora se empieza a analizar el contenido y a concatenar

                // en la primera serie se inicializa todo, se establece el primer peso
                if (setCounter == 0) {
                    stringBuilder.append(actualWeight + "Kg(");
                    setCounter = s.getTimesRepeated();//La serie se repite n veces
                }
                //Entrará aquí a partir de la segunda serie
                else {
                    // Si es el mismo peso no se anota ningún peso nuevo.
                    if (previousWeight.equals(actualWeight)) {
                        // Si las repeticiones son las mismas sigue contabilizando series identicas
                        if (previousRepetitions.equals(actualRepetitions)) {
                            setCounter += s.getTimesRepeated();
                        }
                        // Si son distintas se anotan el resultado de la series previas (series x
                        // repeticiones) y se resetea
                        else {
                            stringBuilder.append((setCounter == 1) ? "" : setCounter + "x")
                                    .append(previousRepetitions)
                                    .append(",");// Añade coma para separar las siguientes con el mismo peso
                            setCounter = s.getTimesRepeated();
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


}
