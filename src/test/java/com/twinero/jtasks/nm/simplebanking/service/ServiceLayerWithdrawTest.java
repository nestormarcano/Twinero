package com.twinero.jtasks.nm.simplebanking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.twinero.jtasks.nm.simplebanking.beans.Withdraw;
import com.twinero.jtasks.nm.simplebanking.beans.WithdrawResp;
import com.twinero.jtasks.nm.simplebanking.beans.Session;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.repository.SimpleBankRepository;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceLayerWithdrawTest
{
	@Autowired
	private SimpleBankService service;

	@Autowired
	@MockBean
	private SimpleBankRepository repository;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------ shouldDoWithdraw
	/**
	 * Performs a client's account withdraw.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldDoWithdraw ()
	{
		try
		{
			long clientID = 10;
			Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
			session.setClientID(clientID);

			Withdraw withdraw = new Withdraw();
			withdraw.setClientID(session.getClientID());

			Withdraw withdrawForResp = new Withdraw(123);
			WithdrawResp expectedWithdrawReps = new WithdrawResp(withdrawForResp, WithdrawResp.Status.OK);

			when(repository.doWithdraw(withdraw, session.getSessionID())).thenReturn(expectedWithdrawReps);

			WithdrawResp obtainedDepositResp = service.doWithdraw(withdraw, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedWithdrawReps);
			verify(repository, only()).doWithdraw(withdraw, session.getSessionID());
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
	 * Performs a not valid withdraw (invalid client).
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

			Withdraw withdraw = new Withdraw();
			withdraw.setClientID(session.getClientID());

			Withdraw withdrawForResp = new Withdraw(123);
			WithdrawResp expectedWithdrawReps = new WithdrawResp(withdrawForResp, WithdrawResp.Status.INVALID_CLIENT);

			when(repository.doWithdraw(withdraw, session.getSessionID())).thenReturn(expectedWithdrawReps);

			WithdrawResp obtainedDepositResp = service.doWithdraw(withdraw, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedWithdrawReps);
			verify(repository, only()).doWithdraw(withdraw, session.getSessionID());
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
	 * Performs a not valid withdraw (expired session).
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

			Withdraw withdraw = new Withdraw();
			withdraw.setClientID(session.getClientID());

			Withdraw withdrawForResp = new Withdraw(123);
			WithdrawResp expectedWithdrawReps = new WithdrawResp(withdrawForResp, WithdrawResp.Status.SESSION_EXPIRED);

			when(repository.doWithdraw(withdraw, session.getSessionID())).thenReturn(expectedWithdrawReps);

			WithdrawResp obtainedDepositResp = service.doWithdraw(withdraw, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedWithdrawReps);
			verify(repository, only()).doWithdraw(withdraw, session.getSessionID());
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
	 * Performs a not valid withdraw (invalid session - does not exist).
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

			Withdraw withdraw = new Withdraw();
			withdraw.setClientID(session.getClientID());

			Withdraw withdrawForResp = new Withdraw(123);
			WithdrawResp expectedWithdrawReps = new WithdrawResp(withdrawForResp, WithdrawResp.Status.SESSION_DOES_NOT_EXISTS);

			when(repository.doWithdraw(withdraw, session.getSessionID())).thenReturn(expectedWithdrawReps);

			WithdrawResp obtainedDepositResp = service.doWithdraw(withdraw, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedWithdrawReps);
			verify(repository, only()).doWithdraw(withdraw, session.getSessionID());
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
	 * Performs a not valid withdraw (server error).
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

			Withdraw withdraw = new Withdraw();
			withdraw.setClientID(session.getClientID());

			Withdraw withdrawForResp = new Withdraw(123);
			WithdrawResp expectedWithdrawReps = new WithdrawResp(withdrawForResp, WithdrawResp.Status.SERVER_ERROR);

			when(repository.doWithdraw(withdraw, session.getSessionID())).thenReturn(expectedWithdrawReps);

			WithdrawResp obtainedDepositResp = service.doWithdraw(withdraw, session.getSessionID());

			assertThat(obtainedDepositResp).isEqualTo(expectedWithdrawReps);
			verify(repository, only()).doWithdraw(withdraw, session.getSessionID());
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

