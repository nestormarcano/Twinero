package com.twinero.jtasks.nm.simplebanking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.twinero.jtasks.nm.simplebanking.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.beans.SignupResp;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.repository.SimpleBankRepository;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SignupServiceTest
{
	@Autowired
	private SimpleBankService service;
	
	@Autowired
	@MockBean
	private SimpleBankRepository repository;

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

			Mockito.when(repository.signup(sign)).thenReturn(resp);
			
			SignupResp respFromService = service.signup(sign);

			assertThat(respFromService).isEqualTo(resp);
			verify(repository, only()).signup(sign);
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
			catch(SimpleBankServiceException ex) {
				verify(repository, times(0)).signup(sign);
			}

			verify(repository, times(0)).signup(sign);
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

			when(repository.signup(sign)).thenReturn(resp);
			
			SignupResp respFromService = service.signup(sign);

			assertThat(respFromService).isEqualTo(resp);
			verify(repository, only()).signup(sign);
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
