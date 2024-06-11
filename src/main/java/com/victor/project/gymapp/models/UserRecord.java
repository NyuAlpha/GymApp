package com.victor.project.gymapp.models;

import java.time.LocalDate;

import com.victor.project.gymapp.dto.UserRecordDto;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_records")
public class UserRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Short height;// Altura del usuario

    private Float weight;// Peso del usuario

    private LocalDate date;// Fecha en la que se hace el registro

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;// Usuario asociado a los detalles de usuario

    // Constructor especializado en transformar un Dto a Entity, copia los datos del
    // Dto en la Entity
    public UserRecord(UserRecordDto userRecordDto) {
        update(userRecordDto);
    }

    public UserRecordDto getSimpleDto() {

        UserRecordDto userRecordDto = new UserRecordDto();

        userRecordDto.setId(id);
        userRecordDto.setHeight(height);
        userRecordDto.setWeight(weight);
        userRecordDto.setDate(date);

        return userRecordDto;
    }

    // Actualiza los campos de esta entidad
    public void update(UserRecordDto userRecordDto) {
        // Campos no nulos
        date = userRecordDto.getDate();

        // Campos que pueden ser nulos
        height = userRecordDto.getHeight();
        weight = userRecordDto.getWeight();

        System.out.println("Nuevos campos -> " + date.toString() + "   " + height + "   " + weight);
    }

}
