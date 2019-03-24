package com.twinero.jtasks.nm.simplebanking.repository.beans.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail
{
	String message() default "{repository.beans.annotations.UniqueEmail.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}