package com.victor.project.gymapp.controllers.api;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victor.project.gymapp.dto.ExerciseDto;
import com.victor.project.gymapp.services.IExerciseService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiController {

    private IExerciseService exerciseService;


    @GetMapping("/exercise/{exerciseId}")
    public ResponseEntity<?> getMethodName(@PathVariable Integer exerciseId) {
        return exerciseService.getExercise(exerciseId);
    }


    
}
