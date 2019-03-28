package com.twinero.jtasks.nm.simplebanking.repository.beans.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Indicates that an email field is unique in the BD.
 * @author Nestor Marcano.
 */
// --------------------------------------------------------------------------------------------------------------------
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail
{
	String field() default "{repository.beans.annotations.UniqueEmail}";
	
	String message() default "{repository.beans.annotations.UniqueEmail.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}