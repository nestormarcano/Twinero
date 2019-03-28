package com.twinero.jtasks.nm.simplebanking.web.service;

import static org.junit.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.twinero.jtasks.nm.simplebanking.repository.SignupsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SignDAO;
import com.twinero.jtasks.nm.simplebanking.repository.beans.annotations.UniqueEmailValidator;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceLayerSignupTest
{
	@Autowired
	private SimpleBankService service;

	@Autowired
	@MockBean
	private SignupsRepository signupsRepository;

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------- shouldSignupAndReturnOK
	/**
	 * Performs a valid SignDAO-up.
	 * Runs with: mvn -Dtest=ServiceLayerSignupTest#shouldSignupAndReturnOK test
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldSignupAndReturnOK ()
	{
		try
		{
			String email = "nestor.marcano@gmail.com";
			String password = "123456";

			SignDAO sign = new SignDAO(email, password);

			SignDAO expectedSign = new SignDAO(email, password);
			expectedSign.setSignID(123L);

			when(signupsRepository.save(sign)).thenReturn(expectedSign);

			SignDAO obtainedSign = service.signup(sign);

			assertThat(obtainedSign).isEqualTo(expectedSign);
			verify(signupsRepository, times(1)).save(sign);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------- shouldNotSignupEmailNull
	/**
	 * Performs a not valid sign up because the sign parameter contains null.
	 * Runs with: mvn -Dtest=ServiceLayerSignupTest#shouldNotSignupEmailNull test
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotSignupEmailNull ()
	{
		try
		{
			SignDAO sign = new SignDAO();

			try
			{
				service.signup(sign);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(signupsRepository, times(0)).save(sign);
			}

			verify(signupsRepository, times(0)).save(sign);
		}

		// Error handling
		// --------------
		catch (ConstraintViolationException ex)
		{
			boolean rightException = false;
			Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
			for (ConstraintViolation<?> constraintViolation : violations)
			{
				if (constraintViolation.getMessageTemplate().equals(UniqueEmailValidator.EMAIL_IS_NULL))
				{
					rightException = true;
					break;
				}
			}

			assertTrue(rightException);
		}

		// --------------
		catch (Exception ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------- shouldNotSignupEmailMalformed
	/**
	 * Performs a not valid sign up because the sign email is malformed.
	 * Runs with: mvn -Dtest=ServiceLayerSignupTest#shouldNotSignupEmailMalformed test
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotSignupEmailMalformed ()
	{
		try
		{
			// -------------------------------------------------------------------
			String email = "nestor marcano@gmail.com";
			String password = "123456";

			SignDAO sign = new SignDAO(email, password);
			try
			{
				service.signup(sign);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(signupsRepository, times(0)).save(sign);
			}

			verify(signupsRepository, times(0)).save(sign);
		}

		// Error handling
		// --------------
		catch (ConstraintViolationException ex)
		{
			boolean rightException = false;
			Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
			for (ConstraintViolation<?> constraintViolation : violations)
			{
				if (constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName()
						.equals("javax.validation.constraints.Pattern"))
				{
					rightException = true;
					break;
				}
			}

			assertTrue(rightException);
		}

		// --------------
		catch (Exception ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------- shouldNotSignupPasswordMalformed
	/* *
	 * Performs a not valid sign up because the sign password is malformed.
	 * Runs with: mvn -Dtest=ServiceLayerSignupTest#shouldNotSignupPasswordMalformed test
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotSignupPasswordMalformed ()
	{
		try
		{
			String email = "nestor.marcano@gmail.com";
			String password = "123 456";
			
			SignDAO sign = new SignDAO(email, password);
			
			try
			{
				service.signup(sign);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(signupsRepository, times(0)).save(sign);
			}
	
			verify(signupsRepository, times(0)).save(sign);
		}
	
		// Error handling
		// --------------
		catch (ConstraintViolationException ex)
		{
			boolean rightException = false;
			Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
			for (ConstraintViolation<?> constraintViolation : violations)
			{
				if (constraintViolation.getMessageTemplate().equals("{web.beans.annotations.Password.message}"))
				{
					rightException = true;
					break;
				}
			}

			assertTrue(rightException);
		}

		// --------------
		catch (Exception ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------- shouldNotSignupAlreadyExist
	/* *
	 * Performs a not valid sign up because the client already exists.
	 * Runs with: mvn -Dtest=ServiceLayerSignupTest#shouldNotSignupAlreadyExist test
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotSignupAlreadyExist ()
	{
		try
		{
			long signID = 123L;
			String email = "nestor.marcano@gmail.com";
			String password = "123456";
			
			SignDAO sign = new SignDAO(email, password);
			SignDAO signWithoutPass = new SignDAO(email, null);
			SignDAO expectedSignDAO = new SignDAO(email, password);
	
			SignDAO foundSignDAO = new SignDAO(signID);
			foundSignDAO.setEmail(email);
			Optional<SignDAO> foundOptionalSignDAO = Optional.of(foundSignDAO);
			
			when(signupsRepository.findOne(Example.of(signWithoutPass))).thenReturn(foundOptionalSignDAO);
	
			SignDAO obtinedSignDAO = service.signup(sign);
	
			assertThat(obtinedSignDAO).isEqualTo(expectedSignDAO);
			verify(signupsRepository, only()).findOne(Example.of(signWithoutPass));
		}
	
		// Error handling
		// --------------
		catch (ConstraintViolationException ex)
		{
			boolean rightException = false;
			Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
			for (ConstraintViolation<?> constraintViolation : violations)
			{
				if (constraintViolation.getMessageTemplate().equals("{repository.beans.annotations.UniqueEmail.message}"))
				{
					rightException = true;
					break;
				}
			}

			assertTrue(rightException);
		}

		// --------------
		catch (Exception ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
	}
}
