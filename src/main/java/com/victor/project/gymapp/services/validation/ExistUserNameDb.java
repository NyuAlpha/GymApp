package com.victor.project.gymapp.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ExistUserNameDbValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistUserNameDb {
	String message() default "Ya existe un usuario con ese nombre";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
