package com.victor.project.gymapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un rol de un usuario, perfecto para darle autorización y permisos con Spring Security
 * 
 * @author Víctor José Pérez Blanco
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "roles")
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//Id del rol, solo tiene utilidad a nivel de base de datos

    @Column(nullable = false, unique = true, length = 50)
    private String name;//Nombre del rol

    
}
