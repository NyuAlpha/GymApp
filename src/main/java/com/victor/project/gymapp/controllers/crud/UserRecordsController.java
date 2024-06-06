package com.victor.project.gymapp.controllers.crud;

import java.time.LocalDate;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

@Controller
@RequestMapping(value = "/app/user_record")
public class UserRecordsController {

    private UserRecordService userRecordService;
    private UserService userService;

    public UserRecordsController(UserRecordService userRecordService, UserService userService) {
        this.userRecordService = userRecordService;
        this.userService = userService;
    }

    // Obtiene la lista de registros de usuario paginados y ordenados por fecha
    // descendientemente
    @GetMapping(path = "/list")
    public String getRecords(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {

        // Obtiene los registros y los pagina de forma ordenada
        Pageable pageable = PageRequest.of(page, 10, Sort.by("date").descending());
        Page<UserRecord> userRecord = userRecordService.findAllUserRecord(pageable);
        // Convierte la lista a Dto para servirlo a la vista
        Page<UserRecordDto> userRecordDto = userRecord.map(r -> r.getSimpleDto());
        PageRender<UserRecordDto> pageRender = new PageRender<>("/app/user_record/list", userRecordDto);

        // Pasa los datos a la vista
        model.addAttribute("userRecords", userRecordDto);
        model.addAttribute("page", pageRender);

        return "user_record/user_record_list";
    }

    // Devuelve el formulario necesario para crear un nuevo registro de usuario
    @GetMapping(path = "/create")
    public String getCreateForm(Model model) {

        // Antes de nada cargamos el último registro para preestablecer campos que
        // pueden no variar como la altura, se convierte a dto, se pone la fecha
        // actual y se manda a la vista
        UserRecord lastUserRecord = userRecordService.getLastUserRecordByUser();
        UserRecordDto userRecordDto = lastUserRecord.getSimpleDto();
        userRecordDto.setDate(LocalDate.now());
        model.addAttribute("userRecord", userRecordDto);

        return "user_record/user_record_create";
    }

    // Recupera los datos del formulario, los valida y crea el nuevo registro de
    // usuario, luego devuelve la vista de dicho registro
    @PostMapping(path = "/create")
    public String createUserRecord(Model model, @ModelAttribute("userRecord") @Valid UserRecordDto userRecordDto,
            BindingResult result, RedirectAttributes redirectAttributes) {

        // Validación de formulario
        if (result.hasErrors()) {
            model.addAttribute("userRecord", userRecordDto);
            return "user_record/user_record_create";
        }

        userRecordService.saveUserRecord(userRecordDto);
        redirectAttributes.addFlashAttribute("success", "¡Registro de usuario guardado con exito!");
        return "redirect:/app/user_record/list";
    }

    // Carga la vista con el registro de usuario cargado correspondiente al id
    @GetMapping(path = "/update/{id}")
    public String getUserRecord(Model model, @PathVariable("id") Long userRecordId) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si
        // es, continua.
        if (!userService.checkUserForUserRecordId(userRecordId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        // Carga el registro, lo convierte a Dto y lo envia a la vista
        UserRecordDto userRecordDto = userRecordService.getUserRecord(userRecordId).getSimpleDto();
        model.addAttribute("userRecord", userRecordDto);

        return "user_record/user_record_update";
    }

    // Actualiza el registro y recarga la lista de registros
    @PutMapping(path = "/update/{id}")
    public String updateRecord(Model model, @PathVariable("id") Long userRecordId,
            @ModelAttribute("userRecord") @Valid UserRecordDto userRecordDto, BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Se comprueba si el usuario es el propietario, si no lo es lanza error 403, si
        // es, continua.
        if (!userService.checkUserForUserRecordId(userRecordId))
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");

        // Validación de formulario
        if (result.hasErrors()) {
            model.addAttribute("userRecord", userRecordDto);
            return "user_record/user_record_create";
        }

        userRecordService.updateUserRecord(userRecordDto, userRecordId);
        redirectAttributes.addFlashAttribute("success", "¡Registro de usuario actualizado con exito!");
        return "redirect:/app/user_record/list";
    }

    // Elimina el registro de usuario que coincide con el id enviado
    @DeleteMapping(path = "/delete/{id}")
    public String deleteRecord(Model model, @PathVariable("id") Long userRecordId,
            RedirectAttributes redirectAttributes) {
        // Se comprueba si el usuario es el propietario, si no lo es lanzará un mensaje
        // de acceso denegado
        if (!userService.checkUserForUserRecordId(userRecordId)) {
            redirectAttributes.addFlashAttribute("error", "¡No tienes permiso para realizar esta acción!");
            return "redirect:/app/user_record/list";
        }

        userRecordService.deleteUserRecord(userRecordId);
        redirectAttributes.addFlashAttribute("success", "¡Registro de usuario eliminado con exito!");
        return "redirect:/app/user_record/list";
    }

}
