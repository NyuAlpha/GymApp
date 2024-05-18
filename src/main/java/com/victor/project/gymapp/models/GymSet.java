package com.victor.project.gymapp.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="set_order", nullable = false) 
    private Integer setOrder;//Indica el orden que ocupa en la lista de las series, jamás puede ser nulo

    private Float weight;//El peso a levantar, puede ser nulo en caso

    private Integer repetitions;//Repeticiones de la serie, es nulo cuando se desconoce o se llega al fallo

    @Column(nullable=false, columnDefinition = "boolean default false")
    private Boolean failure;

    @ManyToOne
    @JoinColumn(name="exercise_id",nullable = false)
    private Exercise exercise;//comentario asociado a un ejercicio


}
