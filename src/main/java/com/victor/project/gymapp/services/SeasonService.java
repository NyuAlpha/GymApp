package com.victor.project.gymapp.services;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.dto.SeasonDto;
import com.victor.project.gymapp.models.Season;
import com.victor.project.gymapp.models.User;
import com.victor.project.gymapp.repositories.SeasonRepository;
import com.victor.project.gymapp.repositories.UserRepository;

import lombok.AllArgsConstructor;


/*
 * Servicio para manipular y procesar temporadas de entrenamientos
 */
@Service
@AllArgsConstructor
public class SeasonService implements ISeasonService {

    //Repositorios necesarios
    private SeasonRepository seasonRepository;
    private UserRepository userRepository;
    private UserService userService;


    /*
     * Encuentra todas las temporadas del usuario logeado paginadas
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Season> findAllSeasons(Pageable pageable) {
        return seasonRepository.findByUserUuid(userService.getCurrentUserUuid(), pageable);
    }



    /*
     * Crea una temporada en base al dto y la guarda, luego la devuelve
     */
    @Override
    @Transactional
    public Season saveSeason(SeasonDto seasonDto) {

        //Se crea un Season en base a su dto
        Season season = new Season(seasonDto);

        // Obtiene el usuario de la sesión actual para asignarseo a la temporada
        User user = userRepository.findByUuid(userService.getCurrentUserUuid())
                .orElseThrow(() -> new NoSuchElementException("Usuario actual no encontrado"));
        season.setUser(user);

        // Guarda y devuelve
        return seasonRepository.save(season);
    }





    /*
     * Devuelve una temporada en base a su id
     */
    @Override
    @Transactional(readOnly = true)
    public Season getSeason(Integer seasonId) {
        return seasonRepository.findById(seasonId).orElseThrow(NoSuchElementException::new);
    }




    /*
     * Actualiza la temporada usando los datos del dto
     */
    @Override
    @Transactional
    public Season updateSeason(SeasonDto seasonDto) {

        //Obtiene la temporada de la base de datos y la actualiza con los valores del dto
        Season season = seasonRepository.findById(seasonDto.getId()).orElseThrow(NoSuchElementException::new);
        season.update(seasonDto);

        // Guarda y devuelve
        return seasonRepository.save(season);

    }



    
    /*
     * Borra una temporada mediante su id
     */
    @Override
    @Transactional
    public void deleteSeason(Integer seasonId) {
        seasonRepository.deleteById(seasonId);
    }

}
