package com.victor.project.gymapp.models;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import dto.SeasonDto;
import dto.TrainingDto;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity // Esta anotación indica que la clase es una entidad que se mapeará en una tabla
@Table(name = "seasons") // Opcional: especifica el nombre de la tabla en la base de datos
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "season_comment_id")
    private SeasonComment seasonComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true) 
    private Set<Training> trainings = new HashSet<>();

   
    /*  
        Este método sirve para obtener una instancia dto con los datos de la entidad original de la BBDD.
        IMPORTANTE, que un parámetro sea null no indica que no exista, puede deberse a que no es necesario
        cargarlo desde la BBDD, por ejemplo, no es necesario cargar el comentario cada vez que se necesita
        una temporada.
    */
    public SeasonDto getDto(){

        SeasonDto seasonDto = getSimpleDto();

        if (seasonComment != null) //Si tiene un comentario se asigna
            seasonDto.setSeasonComment(seasonComment.getComment());

        if(user != null)
            seasonDto.setIdUser(user.getUuid());

        if(trainings != null){
            //Obtenemos los entrenamientos si existen, los transformamos a dto y los ordenamos por fecha
            seasonDto.setTrainingsDto(
                trainings.stream()
                    .map(t -> TrainingDto.getSimpleDto(t))
                    .sorted(Comparator.comparing(TrainingDto::getDate))
                    .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }

        return seasonDto;

    }


    //Obtiene los campos elementales para mostrar la temporada en una lista, título, id y fechas.
    public SeasonDto getSimpleDto() {
        SeasonDto seasonDto = new SeasonDto();

        //Parámetros no nulos
        seasonDto.setId(id);
        seasonDto.setTitle(title);
        seasonDto.setStartDate(startDate);

        //Parámetros nullables para el dto
        if (endDate != null)
            seasonDto.setEndDate(endDate);

        return seasonDto;
    }
}
