package com.poseidon.app.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = IsIntegerValidator.class)
public @interface IsInteger {

	String message() default "Please enter an Integer";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
