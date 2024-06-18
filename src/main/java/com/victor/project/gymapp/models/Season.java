package com.victor.project.gymapp.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import com.victor.project.gymapp.dto.SeasonDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/*
 * Esta clase representa una temporada de entrenamientos y sus campos, además es una tabla de la BBDD
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity // Esta anotación indica que la clase es una entidad que se mapeará en una tabla
@Table(name = "seasons") // Opcional: especifica el nombre de la tabla en la base de datos
public class Season {


    //Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;//Titulo de una temporada, es obligatorio

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;//Fecha de inicio de una temporada, es obligatoria

    @Column(name = "end_date")
    private LocalDate endDate; //Fecha en la que finaliza la temporada, no es requerida al comienzo

    @Column(length = 255, name="season_comment")
    private String seasonComment;// comentario asociado a la temporada, no es obligatorio

    //usuario asociado, no cargará cuando se solicite, en principio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Entrenamientos asociados, en principio no cargarán
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Training> trainings = new HashSet<>();








    /*
     * Constructor básico para crear un Season a aprtir de un DTO
     */
    public Season(SeasonDto seasonDto){
        id = null; //El id siempre debe ser nulo cuando se crea, la BBDD se lo asigna
        update(seasonDto);
    }







    /*
     * Obtiene los campos elementales de la entiedad y los convierte a dto para enviarlos a las vistas
     */
    public SeasonDto getDto() {
        SeasonDto seasonDto = new SeasonDto();

        seasonDto.setId(id);
        seasonDto.setTitle(title);
        seasonDto.setStartDate(startDate);
        seasonDto.setEndDate(endDate);
        seasonDto.setSeasonComment(seasonComment);

        return seasonDto;
    }





    

    /*
     * Actualiza los campos simples de esta entidad en base a los datos del Dto
     */
    public void update(SeasonDto seasonDto){

        //Mapea los campos desde el dto

        //Obligatorios, no se comprueba
        startDate = seasonDto.getStartDate();
        title = seasonDto.getTitle();

        //Si vienen vacios se pasan a null en la BBDD
        LocalDate eD = seasonDto.getEndDate();
        endDate = (eD==null)?  null : eD;

        String c = seasonDto.getSeasonComment();
        seasonComment = (c.isBlank())?  null : c;
    }


        
    
}
