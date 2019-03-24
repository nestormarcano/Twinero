package com.twinero.jtasks.nm.simplebanking.repository.beans.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.data.domain.Example;

import com.twinero.jtasks.nm.simplebanking.repository.SignupsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SignDAO;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String>
{
	private SignupsRepository signupsRepository;

	public UniqueEmailValidator ( SignupsRepository signupsRepository )
	{
		this.signupsRepository = signupsRepository;
	}

	@Override
	public boolean isValid (String email,
									ConstraintValidatorContext context )
	{
		return email != null && !signupsRepository.findOne(Example.of(new SignDAO(email, null))).isPresent();
	}
}