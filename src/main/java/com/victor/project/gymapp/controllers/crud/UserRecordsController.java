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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.victor.project.gymapp.dto.UserRecordDto;
import com.victor.project.gymapp.models.UserRecord;
import com.victor.project.gymapp.services.UserRecordService;
import com.victor.project.gymapp.services.UserService;
import com.victor.project.gymapp.util.paginator.PageRender;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;




/*
 * Este controlador maneja las rutas con las que se manipula y muestran los registro de usuario
 * para un usuario concreto
 */
@Controller
@RequestMapping(value = "/app/user_record")
@AllArgsConstructor
public class UserRecordsController {




    //Los servicios requeridos
    private UserRecordService userRecordService;
    private UserService userService;






    

    /*
     * Muestra la vista de registros de usuario, con todos los registros paginados y un 
     * formulario para crear y editarlos
     */
    @GetMapping(path = "/list")
    public String getRecords(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {

        // Obtiene los registros y los pagina de forma ordenada
        Pageable pageable = PageRequest.of(page, 10, Sort.by("date").descending());
        Page<UserRecord> userRecord = userRecordService.findAllUserRecord(pageable);
        // Convierte la lista a una lista de Dto para servirlo en la vista
        Page<UserRecordDto> userRecordDtos = userRecord.map(r -> r.getDto());
        PageRender<UserRecordDto> pageRender = new PageRender<>("/app/user_record/list", userRecordDtos);

        // Pasa los datos a la vista
        model.addAttribute("userRecords", userRecordDtos);
        model.addAttribute("page", pageRender);


        // Antes de nada cargamos el último registro para preestablecer campos que pueden no variar 
        //como la altura, se convierte a dto, se pone la fecha actual y se manda a la vista, solo si no hubo error de validación
        if(!model.containsAttribute("userRecordCreate")){
            UserRecord lastUserRecord = userRecordService.getLastUserRecordByUser();
            UserRecordDto userRecordDto = lastUserRecord.getDto();
            userRecordDto.setDate(LocalDate.now());
            model.addAttribute("userRecordCreate", userRecordDto);
        }

        //Retorna a la vista
        return "user_record/user_record_list";
    }
















    /*
     *Recupera los datos del formulario, los valida y crea el nuevo registro de
     *usuario, redirigue a la misma página con el nuevo registro en la lista
     */
    @PostMapping(path = "/create")
    public String createUserRecord(Model model, @ModelAttribute("userRecord") @Valid UserRecordDto userRecordDto,
            BindingResult result, RedirectAttributes flash) {

        //No requiere checkear el usuario puesto que se asigna siempre el logeado

        // Validación de formulario
        if (result.hasErrors()) {
            flash.addFlashAttribute("org.springframework.validation.BindingResult.userRecordCreate", result);
            flash.addFlashAttribute("userRecordCreate", userRecordDto);
            return "redirect:/app/user_record/list";
        }

        //Guarda el registro
        userRecordService.saveUserRecord(userRecordDto);
        flash.addFlashAttribute("success", "¡Registro de usuario creado con exito!");
        return "redirect:/app/user_record/list";
    }













    /*
     *Recupera los datos del formulario, los valida y actualiza el registro existente de
     *usuario, redirigue a la misma página con el nuevo registro en la lista
     */
    @PutMapping(path = "/update")
    public String updateRecord(Model model,  @ModelAttribute("userRecord") @Valid UserRecordDto userRecordDto, 
                        BindingResult result, RedirectAttributes flash) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForUserRecordId(userRecordDto.getId()))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        // Validación de formulario, devuelve el error en la redirección para mostrarlo
        if (result.hasErrors()) {
            flash.addFlashAttribute("org.springframework.validation.BindingResult.userRecordUpdate", result);
            flash.addFlashAttribute("userRecordUpdate", userRecordDto);
            return "redirect:/app/user_record/list";
        }

        //Actualiza y redirecciona a la lista de registros de usuario
        userRecordService.updateUserRecord(userRecordDto);
        flash.addFlashAttribute("success", "¡Registro de usuario actualizado con exito!");
        return "redirect:/app/user_record/list";
    }









    

    /*
     * Elimina el registro de usuario en base al id, redirecciona a la misma lista
     */
    @DeleteMapping(path = "/delete")
    public String deleteRecord(Model model, @RequestParam("id") Integer userRecordId,
            RedirectAttributes redirectAttributes) {
        
        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si es, continua.
        if (!userService.checkUserForUserRecordId(userRecordId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        //Lo borra y redirecciona a la lista de registros
        userRecordService.deleteUserRecord(userRecordId);
        redirectAttributes.addFlashAttribute("success", "¡Registro de usuario eliminado con exito!");
        return "redirect:/app/user_record/list";
    }

}
