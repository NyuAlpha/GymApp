package com.victor.project.gymapp.models;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name="exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)//Un ejercicio puede tener un solo nombre, pero los nombres pueden estar en muchos ejercicios
    @JoinColumn(name="exercise_name_id", nullable = false)
    private ExerciseName exerciseName;//Nombre del ejercicio, es necesario cargarlo siempre

    @Column(length = 30)
    private String variant;//Un ejercicio puede tener variaciones o elementos adicionales

    @OneToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL, orphanRemoval = true)
    private ExerciseComment exerciseComment;//comentario asociado a un ejercicio

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="training_id", nullable = false)
    private Training training;//Entrenamiento al que pertenece

    @OneToMany(mappedBy = "exercise",fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval=true)
    private Set<GymSet> gymSets = new HashSet<>();//Series que contiene un ejercicio


}
