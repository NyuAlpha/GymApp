package com.victor.project.gymapp.controllers.crud;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
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
import com.victor.project.gymapp.dto.SeasonDto;
import com.victor.project.gymapp.dto.TrainingDto;
import com.victor.project.gymapp.models.Season;
import com.victor.project.gymapp.models.Training;
import com.victor.project.gymapp.services.ISeasonService;
import com.victor.project.gymapp.services.ITrainingService;
import com.victor.project.gymapp.services.IUserService;
import com.victor.project.gymapp.util.paginator.PageRender;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;






/*
 * Este controlador maneja las rutas con las que se manipula y muestran las temporadas y cada temporada individual 
 * para un usuario concreto
 */
@Controller
@RequestMapping({"app/season" , "/", "", "home" })
@AllArgsConstructor
public class SeasonController {


    //Los servicios requeridos
    private ISeasonService seasonService;
    private ITrainingService trainingService;
    private IUserService userService;









    //Muestra la vista con la lista de temporadas paginada y un formulario para crear una temporada
    @GetMapping(path = { "/list", "/", "" })
    public String getSeasonsList(Model model, @RequestParam(name = "page", defaultValue = "0") int page, HttpSession session) {

        //Obtiene las páginas, transforma su contenido de entidad a dto
        Pageable pageable = PageRequest.of(page, 10, Sort.by("startDate").descending());
        Page<Season> seasons = seasonService.findAllSeasons(pageable);
        Page<SeasonDto> seasonsDto = seasons.map(s -> s.getDto());
        //Establece el page render y la ruta a la que hace referencia
        PageRender<SeasonDto> pageRender = new PageRender<>("/app/season/list", seasonsDto);
        //Se envian a la vista la lista paginada y el pageRender
        model.addAttribute("seasons", seasonsDto);
        model.addAttribute("page", pageRender);


        //Preparando formulario vacio para crear nueva temporada solo si no hay un dto de error de validación
        if(!model.containsAttribute("season")){
            SeasonDto seasonDto = new SeasonDto();
            seasonDto.setStartDate(LocalDate.now());//Establece la fecha de hoy
            model.addAttribute("season", seasonDto);
        }
        
        //Devuelve la vista
        return "seasons/seasons_list";
    }






    




    /*
     * Crea y guarda un entrenamiento y redirecciona a la vista de la temporada
     */
    @PostMapping(path = "/create")
    public String processSeason(Model model, @ModelAttribute("season") @Valid SeasonDto seasonDto, BindingResult result,
            RedirectAttributes flash) {

        //Validación y redirección en caso de error
        if (result.hasErrors()) {
            flash.addFlashAttribute("org.springframework.validation.BindingResult.season", result);
            flash.addFlashAttribute("season", seasonDto);// Lo reenvia al formulario
            return "redirect:/app/season/list";
        }

        // Guardamos y conseguimos el id para redireccionar a la vista de la temporada
        Integer seasonId = seasonService.saveSeason(seasonDto).getId();
        flash.addFlashAttribute("success", "Temporada creada con exito");
        return "redirect:/app/season/" + seasonId + "/show";
    }











    /*
     * Carga una temporada y sus detalles por su id y muestra sus entrenamientos paginados si existen
     */
    @GetMapping("{seasonId}/show")
    public String showSeason(Model model, @PathVariable("seasonId") Integer seasonId , 
                            @RequestParam(name = "page", defaultValue = "0") int page) {


        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForSeasonId(seasonId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");


        // Ahora se carga la temporada con el mismo id, carga también el comentario
        // se envia a la vista
        SeasonDto seasonDto = seasonService.getSeason(seasonId).getDto();
        model.addAttribute("season", seasonDto);



        // Ahora procedemos a cargar sus entrenamiento si existen mediante el id de la temporada
        //Se paginan y convierten a dto sus datos
        Pageable pageable = PageRequest.of(page, 20);
        Page<Training> trainings = trainingService.findAllTrainingsBySeasonId(pageable, seasonId);
        Page<TrainingDto> trainingsDto = trainings.map(t -> t.getSimpleDto());


        //Establece el page render y la ruta a la que hace referencia
        PageRender<TrainingDto> pageRender = new PageRender<>("/app/season/" + seasonId, trainingsDto);
        //Se envian los entrenamientos a la vista, la lista paginada y el pageRender
        model.addAttribute("trainings", trainingsDto);
        model.addAttribute("page", pageRender);

        //Training vacio para crear uno desde el formulario, solo si no hay error de validación.
        if(!model.containsAttribute("training")){
            TrainingDto trainingDto = new TrainingDto(LocalDate.now());
            trainingDto.setSeasonId(seasonId);
            model.addAttribute("training", trainingDto);
        }

        //Devuelve la vista
        return "seasons/season";
    }















    /*
     * Muestra la vista para modificar la temporada, enviando un formulario
     */
    @GetMapping(path = "{seasonId}/update")
    public String updateSeasonForm(Model model, @PathVariable("seasonId") Integer seasonId) {


        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForSeasonId(seasonId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        // Ahora se carga la temporada con el mismo id y se envia a la vista
        SeasonDto seasonDto = seasonService.getSeason(seasonId).getDto();
        model.addAttribute("season", seasonDto);
        return "seasons/season_update";
    }














    /*
     * Actualiza los datos de la temporada mediante el formulario enviado
     */
    @PutMapping(path = "/update")
    public String updateSeason(Model model, @ModelAttribute("season") @Valid SeasonDto seasonDto,
                             BindingResult result, RedirectAttributes flash) {


        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForSeasonId(seasonDto.getId()))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        //Validación y redirección en caso de error
        if (result.hasErrors()) {
            model.addAttribute("season", seasonDto);// Lo reenvia al formulario
            return "seasons/season_update";
        }


        // Guardamos y conseguimos el id para redireccionar a la sesión
        seasonService.updateSeason(seasonDto);
        flash.addFlashAttribute("success", "Temporada actualizada con exito");
        return "redirect:/app/season/" + seasonDto.getId() + "/show";
    }














    /*
     * Elimina la temporada mediante su id y redirecciona a la vista de todas las temporadas
     */
    @DeleteMapping(path = "/delete")
    public String deleteSeason(@RequestParam("seasonId") Integer seasonId, RedirectAttributes redirectAttributes) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForSeasonId(seasonId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        //Borra la temporada por id
        seasonService.deleteSeason(seasonId);
        redirectAttributes.addFlashAttribute("success", "Temporada eliminada con exito");
        //Redirigimos a la lista
        return "redirect:/app/season/list";
    }
}
