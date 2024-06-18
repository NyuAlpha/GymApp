package com.victor.project.gymapp.models;

import java.time.LocalDate;

import com.victor.project.gymapp.dto.UserRecordDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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


/*
 * Esta clase representa una entidad de la base de datos y la tabla asociada en ella
 * Almacena datos de usuario importantes para llevar un control de sus estadísticas
 * físicas
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_records")
public class UserRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//Primary key

    @Column(nullable = false)
    private Short height;// Altura del usuario

    @Column(nullable = false)
    private Float weight;// Peso del usuario

    @Column(nullable = false)
    private LocalDate date;// Fecha en la que se hace el registro

    @Column(length = 255, name= "user_record_comment")
    private String userRecordComment;//comentario asociado al registro

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;// Usuario asociado a los detalles de usuario







    /*
     *Constructor especializado en transformar un Dto a Entity, copia los datos del Dto en la Entity
     */ 
    public UserRecord(UserRecordDto userRecordDto) {
        id = null; //Siempre que se crea un UserRecord desde 0 el id es null, será la BBDD quien lo establezca
        update(userRecordDto);
    }






    /*
     * Convierte los datos de esta entidad a un dto y lo devuelve
     */
    public UserRecordDto getDto() {

        UserRecordDto userRecordDto = new UserRecordDto();

        userRecordDto.setId(id);
        userRecordDto.setHeight(height);
        userRecordDto.setWeight(weight);
        userRecordDto.setDate(date);
        userRecordDto.setUserRecordComment(userRecordComment);

        return userRecordDto;
    }




    /*
     * Actualiza los campos de esta entidad en base a un dto
     */
    public void update(UserRecordDto userRecordDto) {
        
        //Simplemente se mapean los datos
        date = userRecordDto.getDate();
        height = userRecordDto.getHeight();
        weight = userRecordDto.getWeight();

        //Si viene vacio se establece como null
        String c = userRecordDto.getUserRecordComment();
        userRecordComment = (c.isBlank())?  null : c;

    }

}
