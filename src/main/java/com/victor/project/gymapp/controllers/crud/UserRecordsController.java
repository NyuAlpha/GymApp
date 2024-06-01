package com.victor.project.gymapp.controllers.crud;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/app/userRecords")
public class UserRecordsController {

    @GetMapping(path="/list")
    public String getRecords(){

        return "";
    }

}
