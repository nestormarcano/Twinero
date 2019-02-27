package com.twinero.jtasks.nm.simplebanking.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.twinero.jtasks.nm.simplebanking.repository.AccountBalancesRepository;
import com.twinero.jtasks.nm.simplebanking.repository.SessionsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountBalance;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.AccountBalanceResp;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceLayerAccountBalanceTest
{
	@Autowired
	private SimpleBankService service;

	@Autowired
	@MockBean
	private SessionsRepository sessionsRepository;
	
	@Autowired
	@MockBean
	private AccountBalancesRepository accountBalancesRepository;

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------- shouldReturnAccountBalance
	/**
	 * Gets a valid account balance.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldReturnAccountBalance ()
	{
		try
		{
			long clientID = 10;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			
			Session session = new Session(sessionID);
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.OK);

			AccountBalance expectedAccountBalance = new AccountBalance();
			AccountBalanceResp expectedAccountBalanceResp = new AccountBalanceResp(expectedAccountBalance,
					AccountBalanceResp.Status.OK);

			when(sessionsRepository.get(sessionID)).thenReturn(session);
			when(accountBalancesRepository.get(clientID)).thenReturn(expectedAccountBalance);

			AccountBalanceResp obtainedAccountBalanceResp = service.getAccountBalance(clientID, sessionID);

			assertThat(obtainedAccountBalanceResp).isEqualTo(expectedAccountBalanceResp);
			verify(sessionsRepository, only()).get(sessionID);
			verify(accountBalancesRepository, only()).get(clientID);
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
	// ----------------------------------------------------------- shouldNotReturnTheAccountBalanceBecauseExpiredSession
	/**
	 * Doesn't get valid account balance (expired session).
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotReturnTheAccountBalanceBecauseExpiredSession ()
	{
		try
		{
			long clientID = 10;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			
			Session session = new Session(sessionID);
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.EXPIRED);

			AccountBalance expectedAccountBalance = new AccountBalance();
			AccountBalanceResp expectedAccountBalanceResp = new AccountBalanceResp(expectedAccountBalance,
					AccountBalanceResp.Status.SESSION_EXPIRED);

			when(sessionsRepository.get(sessionID)).thenReturn(session);

			AccountBalanceResp obtainedAccountBalanceResp = service.getAccountBalance(clientID, sessionID);

			assertThat(obtainedAccountBalanceResp).isEqualTo(expectedAccountBalanceResp);
			verify(sessionsRepository, only()).get(sessionID);
			verifyNoMoreInteractions(accountBalancesRepository);
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
	// ------------------------------------------------------ shouldNotReturnTheAccountBalanceBecauseSessionDoesNotExist
	/**
	 * Doesn't get valid account balance (session doesn't exist).
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotReturnTheAccountBalanceBecauseSessionDoesNotExist ()
	{
		try
		{
			long clientID = 10;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			
			Session session = new Session(sessionID);
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.NOT_EXISTS);

			AccountBalance expectedAccountBalance = new AccountBalance();
			AccountBalanceResp expectedAccountBalanceResp = new AccountBalanceResp(expectedAccountBalance,
					AccountBalanceResp.Status.SESSION_DOES_NOT_EXIST);

			when(sessionsRepository.get(sessionID)).thenReturn(session);

			AccountBalanceResp obtainedAccountBalanceResp = service.getAccountBalance(clientID, sessionID);

			assertThat(obtainedAccountBalanceResp).isEqualTo(expectedAccountBalanceResp);
			verify(sessionsRepository, only()).get(sessionID);
			verifyNoMoreInteractions(accountBalancesRepository);
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
	// -------------------------------------------------------------- shouldNotReturnTheAccountBalanceBecauseServerError
	/**
	 * Doesn't get valid account balance (server error).
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotReturnTheAccountBalanceBecauseServerError ()
	{
		try
		{
			long clientID = 10;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			
			Session session = new Session(sessionID);
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.OK);

			when(sessionsRepository.get(sessionID)).thenReturn(session);
			when(accountBalancesRepository.get(clientID)).thenThrow(SimpleBankServiceException.class);

			try
			{
				service.getAccountBalance(clientID, sessionID);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(sessionsRepository, only()).get(sessionID);
				clearInvocations(sessionsRepository);
				
				verify(accountBalancesRepository, only()).get(clientID);
				clearInvocations(accountBalancesRepository);
			}

			verifyNoMoreInteractions(accountBalancesRepository);
			verifyNoMoreInteractions(accountBalancesRepository);
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
