package com.victor.project.gymapp.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ExistUserEmailDbValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistUserEmailDb {
	String message() default "Ya existe el email";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
