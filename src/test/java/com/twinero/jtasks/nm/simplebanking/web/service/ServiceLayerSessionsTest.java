package com.twinero.jtasks.nm.simplebanking.web.service;

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

import com.twinero.jtasks.nm.simplebanking.repository.SessionsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceLayerSessionsTest
{
	@Autowired
	private SimpleBankService service;

	@Autowired
	@MockBean
	private SessionsRepository sessionsRepository;
	
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

			when(sessionsRepository.add(sign)).thenReturn(expectedSession);

			Session obtainedSession = service.login(sign);

			assertThat(obtainedSession).isEqualTo(expectedSession);
			verify(sessionsRepository, only()).add(sign);
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

			when(sessionsRepository.add(sign)).thenReturn(expectedSession);

			Session obtainedSession = service.login(sign);

			assertThat(obtainedSession).isEqualTo(expectedSession);
			verify(sessionsRepository, only()).add(sign);
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
				verify(sessionsRepository, times(0)).add(sign);
			}

			verify(sessionsRepository, times(0)).add(sign);
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

			try
			{
				service.login(sign);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(sessionsRepository, times(0)).add(sign);
			}

			verify(sessionsRepository, times(0)).add(sign);
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

			when(sessionsRepository.add(sign)).thenThrow(SimpleBankServiceException.class);
			
			try
			{
				service.login(sign);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(sessionsRepository, only()).add(sign);
				clearInvocations(sessionsRepository);
			}

			verify(sessionsRepository, times(0)).add(sign);
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

