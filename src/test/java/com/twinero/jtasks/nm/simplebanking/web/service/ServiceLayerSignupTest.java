package com.twinero.jtasks.nm.simplebanking.web.service;

import static org.junit.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.twinero.jtasks.nm.simplebanking.repository.SignupsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SignupResp;
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
	private SignupsRepository repository;

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------- shouldSignupAndReturnOK
	/**
	 * Performs a valid Sign-up.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldSignupAndReturnOK ()
	{
		try
		{
			Sign sign = new Sign("nestor.marcano@gmail.com", "123456");
			SignupResp resp = new SignupResp(SignupResp.Status.OK);

			when(repository.add(sign)).thenReturn(resp);

			SignupResp respFromService = service.signup(sign);

			assertThat(respFromService).isEqualTo(resp);
			verify(repository, only()).add(sign);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			assertTrue(false);
		}
		catch (Exception ex)
		{
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------- shouldNotSignupEmailNull
	/**
	 * Performs a not valid sign up because the sign parameter contains null.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotSignupEmailNull ()
	{
		try
		{
			Sign sign = new Sign();
			try
			{
				service.signup(sign);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(repository, times(0)).add(sign);
			}

			verify(repository, times(0)).add(sign);
		}

		// Error handling
		// --------------
		//catch (SimpleBankServiceException ex)
		//{
		//	assertTrue(false);
		//}
		catch (Exception ex)
		{
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------- shouldSignupMalformedEmail
	/**
	 * Performs not valid Sign-up (malformed email).
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldSignupMalformedEmail ()
	{
		try
		{
			Sign sign = new Sign("nestor marcano gmail.com", "123456");

			try
			{
				service.signup(sign);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(repository, times(0)).add(sign);
			}

			verify(repository, times(0)).add(sign);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			assertTrue(false);
		}
		catch (Exception ex)
		{
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------- shouldNotSignupAlreadyExist
	/**
	 * Performs a not valid sign up because the client already exists.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotSignupAlreadyExist ()
	{
		try
		{
			Sign sign = new Sign("nestor.marcano@gmail.com", "123456");
			SignupResp resp = new SignupResp(SignupResp.Status.ALREADY_EXISTS);

			when(repository.add(sign)).thenReturn(resp);

			SignupResp respFromService = service.signup(sign);

			assertThat(respFromService).isEqualTo(resp);
			verify(repository, only()).add(sign);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			assertTrue(false);
		}
		catch (Exception ex)
		{
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------- shouldNotSignupServerError
	/**
	 * Performs not valid Sign-up (server error).
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotSignupServerError ()
	{
		try
		{
			Sign sign = new Sign("nestor.marcano@gmail.com", "123456");

			when(repository.add(sign)).thenThrow(SimpleBankServiceException.class);
			
			try
			{
				service.signup(sign);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(repository, only()).add(sign);
				clearInvocations(repository);
			}

			verify(repository, times(0)).add(sign);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			assertTrue(false);
		}
		catch (Exception ex)
		{
			assertTrue(false);
		}
	}
}
