package com.victor.project.gymapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa un rol de un usuario, perfecto para darle autorización y permisos.
 * 
 * @author Víctor José Pérez Blanco
 */
@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {

    public Role() {
    }


    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//Id del rol, solo tiene utilidad a nivel de base de datos

    @Column(nullable = false, unique = true, length = 50)
    private String name;//Nombre del rol

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }


    
}
