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

import com.twinero.jtasks.nm.simplebanking.repository.SessionsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.WithdrawsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SesionStatus;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Withdraw;
import com.twinero.jtasks.nm.simplebanking.repository.beans.WithdrawResp;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceLayerWithdrawTest
{
	@Autowired
	private SimpleBankService service;
	
	@Autowired
	@MockBean
	private SessionsRepository sessionsRepository;

	@Autowired
	@MockBean
	private WithdrawsRepository withdrawsRepository;

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

			Withdraw expectedWithdraw = new Withdraw(123);
			WithdrawResp expectedWithdrawResp = new WithdrawResp(expectedWithdraw, WithdrawResp.Status.OK);

			when(sessionsRepository.get(session.getSessionID())).thenReturn(SesionStatus.OK);
			when(withdrawsRepository.add(withdraw)).thenReturn(expectedWithdrawResp);

			WithdrawResp obtainedWithdrawResp = service.doWithdraw(withdraw, session.getSessionID());

			assertThat(obtainedWithdrawResp).isEqualTo(expectedWithdrawResp);
			verify(sessionsRepository, only()).get(session.getSessionID());
			verify(withdrawsRepository, only()).add(withdraw);
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

			Withdraw expectedWithdraw = new Withdraw();
			WithdrawResp expectedWithdrawResp = new WithdrawResp(expectedWithdraw, WithdrawResp.Status.INVALID_CLIENT);

			when(sessionsRepository.get(session.getSessionID())).thenReturn(SesionStatus.OK);
			when(withdrawsRepository.add(withdraw)).thenReturn(expectedWithdrawResp);

			WithdrawResp obtainedWithdrawResp = service.doWithdraw(withdraw, session.getSessionID());

			assertThat(obtainedWithdrawResp).isEqualTo(expectedWithdrawResp);
			verify(sessionsRepository, only()).get(session.getSessionID());
			verify(withdrawsRepository, only()).add(withdraw);
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

			Withdraw expectedWithdraw = new Withdraw();
			WithdrawResp expectedWithdrawResp = new WithdrawResp(expectedWithdraw, WithdrawResp.Status.SESSION_EXPIRED);

			when(sessionsRepository.get(session.getSessionID())).thenReturn(SesionStatus.EXPIRED);

			WithdrawResp obtainedWithdrawResp = service.doWithdraw(withdraw, session.getSessionID());

			assertThat(obtainedWithdrawResp).isEqualTo(expectedWithdrawResp);
			verify(sessionsRepository, only()).get(session.getSessionID());
			verifyNoMoreInteractions(withdrawsRepository);
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

			Withdraw expectedWithdraw = new Withdraw();
			WithdrawResp expectedWithdrawResp = new WithdrawResp(expectedWithdraw,
					WithdrawResp.Status.SESSION_DOES_NOT_EXIST);

			when(sessionsRepository.get(session.getSessionID())).thenReturn(SesionStatus.NOT_EXISTS);

			WithdrawResp obtainedWithdrawResp = service.doWithdraw(withdraw, session.getSessionID());

			assertThat(obtainedWithdrawResp).isEqualTo(expectedWithdrawResp);
			verify(sessionsRepository, only()).get(session.getSessionID());
			verifyNoMoreInteractions(withdrawsRepository);
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

			when(sessionsRepository.get(session.getSessionID())).thenReturn(SesionStatus.OK);
			when(withdrawsRepository.add(withdraw)).thenThrow(SimpleBankServiceException.class);

			try
			{
				service.doWithdraw(withdraw, session.getSessionID());
			}
			catch (SimpleBankServiceException ex)
			{
				verify(sessionsRepository, only()).get(session.getSessionID());
				clearInvocations(sessionsRepository);
				
				verify(withdrawsRepository, only()).add(withdraw);
				clearInvocations(withdrawsRepository);
			}

			verifyNoMoreInteractions(sessionsRepository);
			verifyNoMoreInteractions(withdrawsRepository);
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
