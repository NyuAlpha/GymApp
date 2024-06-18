package com.victor.project.gymapp.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/*
 * Esta clase representa el nombre de un ejercicio, será una tabla independiente en la BBDD
 * Esto es debido a que se debe reutilizar el nombre de los ejercicios.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="exercise_names")
public class ExerciseName {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//Primary key

    @Column(length = 30, unique = true, nullable = false)
    private String name;//nombre del ejercicio, estrictamente necesario


    //constructor simple para crear un nombre de ejercicio
    public ExerciseName(String name) {
        this.name= name;
    }
}
