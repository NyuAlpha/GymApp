package com.victor.project.gymapp.services.validation;

import com.victor.project.gymapp.services.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExistUserEmailDbValidator implements ConstraintValidator<ExistUserEmailDb,String>{


    private UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !userService.existByEmail(value);
    }
}

