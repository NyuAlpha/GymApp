package com.victor.project.gymapp.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/*
 * Esta clase representa una Primary perteneciente a GymSet, compuesta por dos datos, el ID del
 * entrenamiento padre y el orden de la serie dentro de dicho entrenamiento 
 */
@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GymSetId implements Serializable {


    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise; //Ejercicio al que hace referencia como FK


    @Column(name = "set_order", nullable = false)
    private Byte setOrder; //Orden de la serie




}
