package com.victor.project.gymapp.controllers.crud;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.victor.project.gymapp.models.Season;
import com.victor.project.gymapp.models.Training;
import com.victor.project.gymapp.services.ISeasonService;
import com.victor.project.gymapp.services.ITrainingService;
import com.victor.project.gymapp.services.IUserService;
import com.victor.project.gymapp.util.paginator.PageRender;
import dto.SeasonDto;
import dto.TrainingDto;
import jakarta.validation.Valid;

@Controller
@RequestMapping({"app/season"})
public class SeasonController {

    private ISeasonService seasonService;
    private ITrainingService trainingService;
    private IUserService userService;






    public SeasonController(ISeasonService seasonService, ITrainingService trainingService, IUserService userService) {
        this.seasonService = seasonService;
        this.trainingService = trainingService;
        this.userService = userService;
    }



    //Muestra la vista con la lista de temporadas paginada
    @GetMapping(path={"/list"})
    public String getSeasonsList(Model model,@RequestParam(name="page",defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, 10,Sort.by("startDate").descending());
        Page<Season> seasons = seasonService.findAllSeasons(pageable);
        Page<SeasonDto> seasonsDto = seasons.map(s -> s.getSimpleDto());

        PageRender<SeasonDto> pageRender = new PageRender<>("/app/season/list", seasonsDto);
        model.addAttribute("seasons",seasonsDto);
        model.addAttribute("page", pageRender);
        return "seasons/seasons_list";
    }



    //Devuelve la vista necesaria para crear un entrenamiento
    @GetMapping(path="/create")
    public String createSeason(Model model){

        SeasonDto seasonDto = new SeasonDto();
        seasonDto.setStartDate(LocalDate.now());

        model.addAttribute("season", seasonDto);
        return "seasons/season_create";
    }


    //Crea y guarda un entrenamiento y redirecciona a la vista del entrenamiento
    @PostMapping(path="/create")
    public String processSeason(Model model, @ModelAttribute("season") @Valid SeasonDto seasonDto , BindingResult result, RedirectAttributes flash){

        if(result.hasErrors()){
            model.addAttribute("season", seasonDto);//Lo reenvia al formulario
            return "seasons/season_create";
        }
        //Guardamos y conseguimos el id para redireccionar a el
        Long trainingId = seasonService.saveSeason(seasonDto).getId();
        flash.addFlashAttribute("success", "Temporada creada con exito");
        return "redirect:/app/season/" + trainingId;
    }


    //Carga una temporada y detalles por su id y muestra sus entrenamientos paginados si existen
    @GetMapping("/{id}")
    public String showSeason(@PathVariable("id") Long seasonId, Model model,@RequestParam(name="page",defaultValue = "0") int page) {

        //Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        userService.checkUserForSeasonId(seasonId);

        //Ahora se carga la temporada con el mismo id
        SeasonDto seasonDto = seasonService.getSeason(seasonId).getDto();
        model.addAttribute("season", seasonDto);


        //Ahora procedemos a cargar sus entrenamiento si existen:
        Pageable pageable = PageRequest.of(page, 20);
        Page<Training> trainings = trainingService.findAllTrainingsBySeasonId(pageable,seasonId);
        Page<TrainingDto> trainingsDto = trainings.map(TrainingDto::getSimpleDto);

        PageRender<TrainingDto> pageRender = new PageRender<>("/app/season/" + seasonId, trainingsDto);
        model.addAttribute("trainings",trainingsDto);
        model.addAttribute("page", pageRender);

        return "seasons/season";
    }

    @GetMapping(path="/update/{id}")
    public String updateSeasonForm(Model model, @PathVariable("id") Long seasonId){

        //Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        userService.checkUserForSeasonId(seasonId);

        //Ahora se carga la temporada con el mismo id
        SeasonDto seasonDto = seasonService.getSeason(seasonId).getDto();
        model.addAttribute("season", seasonDto);


        return "seasons/season_update";
    }




    

    @PutMapping(path="/update/{id}")
    public String updateSeason(Model model,@PathVariable("id") Long seasonId,
                     @ModelAttribute("season") @Valid SeasonDto seasonDto , BindingResult result, RedirectAttributes flash){

        if(result.hasErrors()){
            model.addAttribute("season", seasonDto);//Lo reenvia al formulario
            return "seasons/season_update";
        }

        //Guardamos y conseguimos el id para redireccionar a el
        seasonService.updateSeason(seasonDto,seasonId);
        flash.addFlashAttribute("success", "Temporada actualizada con exito");
        return "redirect:/app/season/" + seasonId;
    }



    @DeleteMapping(path = "/delete/{id}")
    public String deleteSeason(@PathVariable("id") Long seasonId , RedirectAttributes redirectAttributes){

        //Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        userService.checkUserForSeasonId(seasonId);

        seasonService.deleteSeason(seasonId);

        redirectAttributes.addFlashAttribute("success", "Temporada eliminada con exito");
        return "redirect:/app/season/list";
    }
}
