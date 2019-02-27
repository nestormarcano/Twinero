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
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.SignReq;
import com.twinero.jtasks.nm.simplebanking.service.beans.SignupResp;

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
	 * Performs a valid Sign-up.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldSignupAndReturnOK ()
	{
		try
		{
			String email = "nestor.marcano@gmail.com";
			String password = "123456";
			
			Sign sign = new Sign(email, password);
			
			Sign expectedSign = new Sign(email, password);
			expectedSign.setSignID(123);
			
			SignReq signReq = new SignReq(email, password);
			
			SignupResp expectedSignupResp = new SignupResp(SignupResp.Status.OK);
			when(signupsRepository.add(sign)).thenReturn(expectedSign);

			SignupResp obtainedSignupResp = service.signup(signReq);

			assertThat(obtainedSignupResp).isEqualTo(expectedSignupResp);
			verify(signupsRepository, only()).add(sign);
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
			SignReq signReq = new SignReq();
			
			try
			{
				service.signup(signReq);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(signupsRepository, times(0)).add(sign);
			}

			verify(signupsRepository, times(0)).add(sign);
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
			String email = "nestor marcano gmail.com";
			String password = "123456";
			
			Sign sign = new Sign(email, password);
			SignReq signReq = new SignReq(email, password);
			
			try
			{
				service.signup(signReq);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(signupsRepository, times(0)).add(sign);
			}

			verify(signupsRepository, times(0)).add(sign);
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
			String email = "nestor.marcano@gmail.com";
			String password = "123456";
			
			Sign sign = new Sign(email, password);
			Sign expectedSign = new Sign();
			SignReq signReq = new SignReq(email, password);
			
			SignupResp expectedSignupResp = new SignupResp(SignupResp.Status.ALREADY_EXISTS);

			when(signupsRepository.add(sign)).thenReturn(expectedSign);

			SignupResp obtinedSignupResp = service.signup(signReq);

			assertThat(obtinedSignupResp).isEqualTo(expectedSignupResp);
			verify(signupsRepository, only()).add(sign);
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
			String email = "nestor.marcano@gmail.com";
			String password = "123456";
			
			Sign sign = new Sign(email, password);
			SignReq signReq = new SignReq(email, password);

			when(signupsRepository.add(sign)).thenThrow(SimpleBankServiceException.class);
			
			try
			{
				service.signup(signReq);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(signupsRepository, only()).add(sign);
				clearInvocations(signupsRepository);
			}

			verify(signupsRepository, times(0)).add(sign);
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
