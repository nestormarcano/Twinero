package com.twinero.jtasks.nm.simplebanking.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.twinero.jtasks.nm.simplebanking.repository.DepositsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.SessionsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Deposit;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.DepositResp;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceLayerDepositTest
{
	@Autowired
	private SimpleBankService service;
	
	@Autowired
	@MockBean
	private SessionsRepository sessionsRepository;

	@Autowired
	@MockBean
	private DepositsRepository depositsRepository;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------- shouldDoDeposit
	/**
	 * Performs a client's account deposit.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldDoDeposit ()
	{
		try
		{
			long clientID = 10;
			Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.OK);

			Deposit deposit = new Deposit();
			deposit.setClientID(session.getClientID());

			Deposit expectedDeposit = new Deposit(123);
			expectedDeposit.setClientID(session.getClientID());
			DepositResp expectedDepositReps = new DepositResp(expectedDeposit, DepositResp.Status.OK);

			when(sessionsRepository.get(session.getSessionID())).thenReturn(session);
			when(depositsRepository.add(deposit)).thenReturn(expectedDeposit);

			DepositResp obtainedDepositResp = service.doDeposit(deposit, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedDepositReps);
			verify(sessionsRepository, only()).get(session.getSessionID());
			verify(depositsRepository, only()).add(deposit);
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
	// ---------------------------------------------------------------------------- shouldNotDepositBecauseInvalidClient
	/**
	 * Performs a not valid deposit (invalid client).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotDepositBecauseInvalidClient ()
	{
		try
		{
			long clientID = 10;
			Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.OK);

			Deposit deposit = new Deposit();
			deposit.setClientID(session.getClientID());

			Deposit expectedDeposit = new Deposit();
			DepositResp expectedDepositReps = new DepositResp(expectedDeposit, DepositResp.Status.INVALID_CLIENT);

			when(sessionsRepository.get(session.getSessionID())).thenReturn(session);
			when(depositsRepository.add(deposit)).thenReturn(expectedDeposit);

			DepositResp obtainedDepositResp = service.doDeposit(deposit, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedDepositReps);
			verify(sessionsRepository, only()).get(session.getSessionID());
			verify(depositsRepository, only()).add(deposit);
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
	// --------------------------------------------------------------------------- shouldNotDepositBecauseExpiredSession
	/**
	 * Performs a not valid deposit (expired session).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotDepositBecauseExpiredSession ()
	{
		try
		{
			long clientID = 10;
			Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.EXPIRED);

			Deposit deposit = new Deposit();
			deposit.setClientID(clientID);
			deposit.setMount(new BigDecimal(24563.87));
			deposit.setTime(new Date(1551118553000L));

			Deposit expectedDeposit = new Deposit();
			DepositResp expectedDepositResp = new DepositResp(expectedDeposit, DepositResp.Status.SESSION_EXPIRED);

			when(sessionsRepository.get(session.getSessionID())).thenReturn(session);

			DepositResp obtainedDepositResp = service.doDeposit(deposit, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedDepositResp);
			verify(sessionsRepository, only()).get(session.getSessionID());
			verifyNoMoreInteractions(depositsRepository);
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
	// --------------------------------------------------------------------------- shouldNotDepositBecauseInvalidSession
	/**
	 * Performs a not valid deposit (invalid session - does not exist).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotDepositBecauseInvalidSession ()
	{
		try
		{
			long clientID = 10;
			Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.NOT_EXISTS);

			Deposit deposit = new Deposit();
			deposit.setClientID(clientID);

			Deposit expectedDeposit = new Deposit();
			DepositResp expectedDepositResp = new DepositResp(expectedDeposit, DepositResp.Status.SESSION_DOES_NOT_EXIST);

			when(sessionsRepository.get(session.getSessionID())).thenReturn(session);

			DepositResp obtainedDepositResp = service.doDeposit(deposit, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedDepositResp);
			verify(sessionsRepository, only()).get(session.getSessionID());
			verifyNoMoreInteractions(depositsRepository);
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
	// ------------------------------------------------------------------------------ shouldNotDepositBecauseServerError
	/**
	 * Performs a not valid deposit (server error).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotDepositBecauseServerError ()
	{
		try
		{
			long clientID = 10;
			Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.OK);

			Deposit deposit = new Deposit();
			deposit.setClientID(clientID);

			when(sessionsRepository.get(session.getSessionID())).thenReturn(session);
			when(depositsRepository.add(deposit)).thenThrow(SimpleBankServiceException.class);

			try
			{
				service.doDeposit(deposit, session.getSessionID());
			}
			catch (SimpleBankServiceException ex)
			{
				verify(sessionsRepository, only()).get(session.getSessionID());
				verify(depositsRepository, only()).add(deposit);
				clearInvocations(depositsRepository);
			}

			verifyNoMoreInteractions(sessionsRepository);
			verifyNoMoreInteractions(depositsRepository);
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
