package com.twinero.jtasks.nm.simplebanking.repository.beans.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.data.domain.Example;

import com.twinero.jtasks.nm.simplebanking.repository.SignupsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SignDAO;

/**
 * The validator for the UniqueEmail constraint.
 * @author Nestor Marcano.
 */
// --------------------------------------------------------------------------------------------------------------------
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String>
{
	public static final String EMAIL_IS_NULL = "email is null";
	
	private SignupsRepository signupsRepository;

	public UniqueEmailValidator ( SignupsRepository signupsRepository )
	{
		this.signupsRepository = signupsRepository;
	}

	@Override
	public void initialize (UniqueEmail constraintAnnotation )
	{
	}

	@Override
	public boolean isValid (String email,
									ConstraintValidatorContext context )
	{
		if (email == null)
		{
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(EMAIL_IS_NULL)
					.addPropertyNode("email")
					.addConstraintViolation();
			return false;
		}

		if (signupsRepository != null)
			return !signupsRepository.findOne(Example.of(new SignDAO(email, null))).isPresent();

		return true;
	}
}