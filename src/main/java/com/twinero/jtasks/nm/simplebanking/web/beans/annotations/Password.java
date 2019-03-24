package com.twinero.jtasks.nm.simplebanking.web.beans.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password
{
	String message() default "{web.beans.annotations.Password.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}