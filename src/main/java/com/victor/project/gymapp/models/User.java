package com.victor.project.gymapp.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.HashSet;
import java.util.Set;


/**
 * Entidad que representa un usuario en el sistema. Cada usuario tiene asignado un
 * identificador único (UUID), un nombre de usuario, correo electrónico, contraseña y un
 * estado que indica si está habilitado o no. Los usuarios pueden tener varios roles que
 * definen sus permisos dentro del sistema.
 *
 * @author Víctor José Pérez Blanco
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")})
public class User {

    
    public User() {
        roles = new HashSet<>();
    }

    public User(String username,String email,String password) {
        this();
        this.username = username;
        this.email= email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    
    @Column(nullable = false, unique = true, length = 50)
    private String username;//Nombre de usuario

    @Column(nullable = true, unique = true, length = 100)
    private String email;//email del usuario, de momento no es obligatorio

    @Column(nullable = false, length = 255)
    private String password;//Contraseña cifrada

    //enabled permite dar de alta o baja a un usuario
    @Column(nullable = false)
    private boolean enabled;

    @ManyToMany
    @JoinTable(
        name = "user_roles",//Nueva tabla intermedia
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "uuid"),//FK
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),//FK
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","role_id"})//Clave primaria compuesta
        
    )
    private Set<Role> roles = new HashSet<>();//Cada usuario puede tener muchos roles, y los roles pueden compartirse en más de un usuario


    @OneToOne(mappedBy ="user" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private UserDetails userDetails;

    @PrePersist
    private void prePersist(){
        enabled = true;
    }


    public void addRole(Role role){
        if(!roles.contains(role)){
            roles.add(role);
        }
    }



}
