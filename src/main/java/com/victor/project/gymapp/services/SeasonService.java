package com.victor.project.gymapp.services;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.models.Season;
import com.victor.project.gymapp.models.SeasonComment;
import com.victor.project.gymapp.models.User;
import com.victor.project.gymapp.repositories.SeasonRepository;
import com.victor.project.gymapp.repositories.UserRepository;

import dto.SeasonDto;

@Service
public class SeasonService implements ISeasonService{


    private SeasonRepository seasonRepository;
    private UserRepository userRepository;


    
    public SeasonService(SeasonRepository seasonRepository, UserRepository userRepository) {
        this.seasonRepository = seasonRepository;
        this.userRepository = userRepository;
    }



    @Override
    @Transactional(readOnly = true)
    public Page<Season> findAllSeasons(Pageable pageable) {
        return seasonRepository.findByUserUuid(getCurrentUserUuid(), pageable);
    }




    //Obtiene una temporada y lo guarda en la BBDD
    @Override
    @Transactional
    public Season saveSeason(SeasonDto seasonDto){
        
        Season season = new Season();

        season.setTitle(seasonDto.getTitle());
        season.setStartDate(seasonDto.getStartDate());

        //Puede tener fecha de fin o no.
        if (seasonDto.getEndDate() != null)
            season.setEndDate(seasonDto.getEndDate());

        if(!seasonDto.getSeasonComment().isBlank())
            season.setSeasonComment(new SeasonComment(seasonDto.getSeasonComment()));

        //Obtiene el usuario de la sesión actual para asignarseo a la temporada
        User user = userRepository.findByUsername(getCurrentUsername()).orElseThrow(NoSuchElementException::new);
        season.setUser(user);
        
        //Guarda y devuelve
        return seasonRepository.save(season);
    }

    @Override
    @Transactional(readOnly = true)
    public Season getSeason(Long seasonId){
        return seasonRepository.findById(seasonId).orElseThrow(NoSuchElementException::new);
    }



    @Override
    @Transactional
    public Season updateSeason(SeasonDto seasonDto, Long seasonId) {

        Season season = seasonRepository.findById(seasonId).orElseThrow(NoSuchElementException::new);

        season.setTitle(seasonDto.getTitle());
        season.setStartDate(seasonDto.getStartDate());

        //Puede tener fecha de fin o no.
        if (seasonDto.getEndDate() != null)
            season.setEndDate(seasonDto.getEndDate());
        else
            season.setEndDate(null);

        
        //Si el formulario trae un comentario
        if(!seasonDto.getSeasonComment().isBlank()){
            //Si ya tenía un comentario asignado simplemente se modifica el texto
            if(season.getSeasonComment() != null){
                season.getSeasonComment().setComment(seasonDto.getSeasonComment());
            }else{
                //Si no se crea un nuevo SeasonComment
                season.setSeasonComment(new SeasonComment(seasonDto.getSeasonComment()));
            }
        }//Si no hay comentario se comprueba si ya tenía previamente, en dicho caso se eliminará el comentario
        else{
            if(season.getSeasonComment() != null)
                season.setSeasonComment(null);
        }

        //Guarda y devuelve
        return seasonRepository.save(season);
        
    }



    @Override
    @Transactional
    public void deleteSeason(Long seasonId) {
        seasonRepository.deleteById(seasonId);
    }


    

}
