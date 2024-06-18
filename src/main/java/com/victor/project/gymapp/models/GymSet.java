package com.victor.project.gymapp.models;


import java.math.BigDecimal;
import org.hibernate.annotations.ColumnDefault;
import com.victor.project.gymapp.dto.GymSetDto;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




/*
 * Esta clase representa una Serie en el contexto de un ejercicio, además es una tabla en la BBDD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="gym_sets")
public class GymSet {


    @EmbeddedId
    private GymSetId id;//Id compuesta por id de ejercicio y orden de la serie

    @Column( precision = 4, scale = 1)  // Configurando precision y scale para DECIMAL(4,1)
    private BigDecimal weight; //Peso de una serie


    private Byte repetitions;//Repeticiones de la serie, es nulo cuando se desconoce o se llega al fallo

    
    @Column(nullable=false, columnDefinition = "boolean default false")
    private Boolean failure;//Si la serie es al fallo muscular o no

    //Esta columna sirve para contabilizar una serie varias veces cuando 
    //sean consecutivas e identicas 60kg(8,8,7) -> 60kg(2x8,7)
    @Column(name = "times_repeated")
    @ColumnDefault("1")//Vale 1 por defecto en caso de dejarse null en un formulario
    private Byte timesRepeated;









    /*
     * constructor básico para crear un GymSet en base a al Dto enviado
     */
    public GymSet(GymSetDto gymSetDto) {
        update(gymSetDto);
    }





    /*
     * Establece el ID compuesto de esta entidad, formada por el id de su ejercicio padre y su orden interno
     * en la lista de series del ejercicio
     */
    public void setId(Exercise e, Byte order){
        id = new GymSetId(e, order);
    }






    /*
     * Se obtiene una instancia de dto de esta entidad para mandarla a las vistas
     */
    public GymSetDto getDto(){

        //Se mapea la entidad al dto, pero antes se convierten algunos datos para mostrarlos
        Integer reps = (repetitions == null)? 0: (int)repetitions;
        BigDecimal weightInt = (weight == null)? BigDecimal.valueOf(0.0):weight;

        GymSetDto gymSetDto = new GymSetDto(id.getExercise().getId(), id.getSetOrder(), weightInt, reps, failure,(int)timesRepeated);
        
        //Se devuelve
        return gymSetDto;
    }









    /*
     * Mapea los datos simples del dto en esta entidad y actualiza sus campos
     */
    public void update(GymSetDto gymSetDto){


        /*
         *    Campos mapeado desde el dto
         */
        
        weight = gymSetDto.getWeight();

        //Las repeticiones pueden venir como null, es importante verificarlo antes de usar byteValue()
        Byte reps = (gymSetDto.getRepetitions()==null || gymSetDto.getRepetitions()==0)? null:gymSetDto.getRepetitions().byteValue();
        repetitions = reps;

        //el número de series pueden venir null, es importante verificarlo antes de usar byteValue()
        //Realmente valdrá 1 en caso de null
        Byte times = (gymSetDto.getTimesRepeated()==null)? 1:gymSetDto.getTimesRepeated().byteValue();
        timesRepeated = times;

        System.out.println("Fallo?" + failure);
        failure = gymSetDto.getFailure();

    }

}
