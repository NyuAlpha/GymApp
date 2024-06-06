package com.victor.project.gymapp.dto;

import com.victor.project.gymapp.models.GymSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GymSetDto {

    private Long id;

    private Long exerciseId;

    private Integer setOrder;// Indica el orden que ocupa en la lista de las series, jamás puede ser nulo

    private Float weight;// El peso a levantar, puede ser nulo en caso

    private Integer repetitions;// Repeticiones de la serie, es nulo cuando se desconoce o se llega al fallo

    private Boolean failure;

    public GymSetDto(Long exerciseId) {
        this.exerciseId = exerciseId;
        failure = false;
    }

    public static GymSetDto getsimpleDto(GymSet gymSet) {
        GymSetDto setDto = new GymSetDto(gymSet.getExercise().getId());
        setDto.setId(gymSet.getId());
        setDto.setSetOrder(gymSet.getSetOrder());
        setDto.setWeight(gymSet.getWeight());
        setDto.setRepetitions(gymSet.getRepetitions());
        setDto.setFailure(gymSet.getFailure());

        return setDto;
    }
}
