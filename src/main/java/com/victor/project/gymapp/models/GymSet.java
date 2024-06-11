package com.victor.project.gymapp.models;


import java.math.BigDecimal;

import com.victor.project.gymapp.dto.GymSetDto;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="gym_sets")
public class GymSet {

    @EmbeddedId
    private GymSetId id;

    @Column( precision = 4, scale = 1)  // Configurando precision y scale para DECIMAL(4,1)
    private BigDecimal weight;

    @Column()
    private Byte repetitions;//Repeticiones de la serie, es nulo cuando se desconoce o se llega al fallo

    //Si la serie es al fallo muscular o no
    @Column(nullable=false, columnDefinition = "boolean default false")
    private Boolean failure;

    //Esta columna sirve para contabilizar una serie varias veces cuando 
    //sean consecutivas e identicas 60kg(8,8,7) -> 60kg(2x8,7)
    @Column(name = "times_repeated")
    private Byte timesRepeated;


    public void setId(Exercise e, Byte order){
        id = new GymSetId();
        id.setExercise(e);
        id.setSetOrder(order);
    }

    public GymSetDto getDto(){

        //Se mapea la entidad al dto, pero antes se convierten algunos datos para mostrarlos
        Integer reps = (repetitions == null)? 0: (int)repetitions;
        BigDecimal weightInt = (weight == null)? BigDecimal.valueOf(0.0):weight;

        GymSetDto gymSetDto = new GymSetDto(id.getExercise().getId(), id.getSetOrder(), weightInt, reps, failure,(int)timesRepeated);
        
        //Se devuelve
        return gymSetDto;
    }


}
