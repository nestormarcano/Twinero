package com.twinero.jtasks.nm.simplebanking.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.twinero.jtasks.nm.simplebanking.beans.Deposit;
import com.twinero.jtasks.nm.simplebanking.beans.DepositResp;
import com.twinero.jtasks.nm.simplebanking.beans.Session;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.repository.SimpleBankRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SesionStatus;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceLayerDepositTest
{
	@Autowired
	private SimpleBankService service;

	@Autowired
	@MockBean
	private SimpleBankRepository repository;

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

			Deposit deposit = new Deposit();
			deposit.setClientID(session.getClientID());

			Deposit depositForResp = new Deposit(123);
			DepositResp expectedDepositReps = new DepositResp(depositForResp, DepositResp.Status.OK);

			when(repository.verifySession(session.getSessionID())).thenReturn(SesionStatus.OK);
			when(repository.doDeposit(deposit)).thenReturn(expectedDepositReps);

			DepositResp obtainedDepositResp = service.doDeposit(deposit, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedDepositReps);
			verify(repository, times(1)).verifySession(session.getSessionID());
			verify(repository, times(1)).doDeposit(deposit);
			verifyNoMoreInteractions(repository);
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

			Deposit deposit = new Deposit();
			deposit.setClientID(session.getClientID());

			Deposit depositForResp = new Deposit(123);
			DepositResp expectedDepositReps = new DepositResp(depositForResp, DepositResp.Status.INVALID_CLIENT);

			when(repository.verifySession(session.getSessionID())).thenReturn(SesionStatus.OK);
			when(repository.doDeposit(deposit)).thenReturn(expectedDepositReps);

			DepositResp obtainedDepositResp = service.doDeposit(deposit, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedDepositReps);
			verify(repository, times(1)).verifySession(session.getSessionID());
			verify(repository, times(1)).doDeposit(deposit);
			verifyNoMoreInteractions(repository);
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

			Deposit deposit = new Deposit(123);
			deposit.setClientID(session.getClientID());

			Deposit expectedDeposit = new Deposit();
			DepositResp expectedDepositResp = new DepositResp(expectedDeposit, DepositResp.Status.SESSION_EXPIRED);

			when(repository.verifySession(session.getSessionID())).thenReturn(SesionStatus.EXPIRED);

			DepositResp obtainedDepositResp = service.doDeposit(deposit, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedDepositResp);
			verify(repository, only()).verifySession(session.getSessionID());
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

			Deposit deposit = new Deposit();
			deposit.setClientID(session.getClientID());

			Deposit expectedDeposit = new Deposit();
			DepositResp expectedDepositResp = new DepositResp(expectedDeposit, DepositResp.Status.SESSION_DOES_NOT_EXIST);

			when(repository.verifySession(session.getSessionID())).thenReturn(SesionStatus.NOT_EXISTS);

			DepositResp obtainedDepositResp = service.doDeposit(deposit, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedDepositResp);
			verify(repository, only()).verifySession(session.getSessionID());
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

			Deposit deposit = new Deposit();
			deposit.setClientID(session.getClientID());

			when(repository.verifySession(session.getSessionID())).thenReturn(SesionStatus.OK);
			when(repository.doDeposit(deposit)).thenThrow(SimpleBankServiceException.class);

			try
			{
				service.doDeposit(deposit, session.getSessionID());
			}
			catch (SimpleBankServiceException ex)
			{
				verify(repository, times(1)).verifySession(session.getSessionID());
				verify(repository, times(1)).doDeposit(deposit);
				verifyNoMoreInteractions(repository);
				clearInvocations(repository);
			}

			verify(repository, times(0)).verifySession(session.getSessionID());
			verify(repository, times(0)).doDeposit(deposit);
			verifyNoMoreInteractions(repository);
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
