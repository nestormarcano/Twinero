package com.twinero.jtasks.nm.simplebanking.web.beans.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements ConstraintValidator<Password, String>
{
	@Override
	public boolean isValid (String password,
									ConstraintValidatorContext constraintValidatorContext )
	{
		return password != null && password.split(" ").length == 1;
	}
}
