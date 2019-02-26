package com.twinero.jtasks.nm.simplebanking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
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

import com.twinero.jtasks.nm.simplebanking.beans.Session;
import com.twinero.jtasks.nm.simplebanking.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.repository.SimpleBankRepository;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceLayerSessionsTest
{
	@Autowired
	private SimpleBankService service;

	@Autowired
	@MockBean
	private SimpleBankRepository repository;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------ shouldLoginAndReturnOK
	/**
	 * Performs a valid login.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldLoginAndReturnOK ()
	{
		try
		{
			String email = "nestor.marcano@gmail.com";
			String password = "123456";
			Sign sign = new Sign(email, password);
			
			long clientID = 10;
			Session expectedSession = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
			expectedSession.setClientID(clientID);

			when(repository.login(sign)).thenReturn(expectedSession);

			Session obtainedSession = service.login(sign);

			assertThat(obtainedSession).isEqualTo(expectedSession);
			verify(repository, only()).login(sign);
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
	// -------------------------------------------------------------------------------------------------- shouldNotLogin
	/**
	 * Performs a valid login.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotLogin ()
	{
		try
		{
			String email = "nestor.marcano@gmail.com";
			String password = "123456";
			Sign sign = new Sign(email, password);
			
			Session expectedSession = new Session();
			expectedSession.setSessionStatus(Session.Status.UNAUTHORIZED);

			when(repository.login(sign)).thenReturn(expectedSession);

			Session obtainedSession = service.login(sign);

			assertThat(obtainedSession).isEqualTo(expectedSession);
			verify(repository, only()).login(sign);
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
	// ----------------------------------------------------------------------------------------- shouldNotLoginEmailNull
	/**
	 * Performs a not valid login because the sign parameter contains null.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotLoginEmailNull ()
	{
		try
		{
			Sign sign = new Sign();
			try
			{
				service.login(sign);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(repository, times(0)).login(sign);
			}

			verify(repository, times(0)).login(sign);
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
	// ------------------------------------------------------------------------------------ shouldNotLoginMalformedEmail
	/**
	 * Performs not valid login (malformed email).
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotLoginMalformedEmail ()
	{
		try
		{
			Sign sign = new Sign("nestor marcano gmail.com", "123456");
			when(repository.signup(sign)).thenThrow(SimpleBankServiceException.class);

			try
			{
				service.login(sign);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(repository, times(0)).login(sign);
			}

			verify(repository, times(0)).login(sign);
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
	// --------------------------------------------------------------------------------------- shouldNotLoginServerError
	/**
	 * Performs not valid login (server error).
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotLoginServerError ()
	{
		try
		{
			Sign sign = new Sign("nestor.marcano@gmail.com", "123456");

			when(repository.login(sign)).thenThrow(SimpleBankServiceException.class);
			
			try
			{
				service.login(sign);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(repository, only()).login(sign);
				clearInvocations(repository);
			}

			verify(repository, times(0)).login(sign);
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

