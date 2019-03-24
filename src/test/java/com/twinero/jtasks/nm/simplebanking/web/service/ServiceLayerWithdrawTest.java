package com.twinero.jtasks.nm.simplebanking.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.twinero.jtasks.nm.simplebanking.repository.SessionsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.MovementsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SessionDAO;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SignDAO;
import com.twinero.jtasks.nm.simplebanking.repository.beans.MovementDAO;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.Session;
import com.twinero.jtasks.nm.simplebanking.service.beans.Movement;
import com.twinero.jtasks.nm.simplebanking.service.beans.MovementResp;

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
	private MovementsRepository transactionsRepository;

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
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			UUID sessionUUID = UUID.fromString(sessionID);
			long withdrawID = 123;

			BigDecimal amount = new BigDecimal(834.54);
			String reference = "276534982";
			Date time = new Date(1547645112000L); // 2019-01-16T09:25:12.000-04:00

			Date sessionExpiredDate = new Date(new Date().getTime() + 100000L);

			Session session = new Session(sessionID);
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.OK);

			SignDAO customer = new SignDAO();

			SessionDAO sessionDAO = new SessionDAO(sessionUUID);
			sessionDAO.setTime(sessionExpiredDate);
			sessionDAO.setCustomer(customer);

			MovementDAO withdrawDAO = new MovementDAO();
			withdrawDAO.setClientID(session.getClientID());
			withdrawDAO.setAmount(amount);
			withdrawDAO.setTime(time);
			withdrawDAO.setType(MovementDAO.Type.WITHDRAW);

			Movement withdraw = new Movement();
			withdraw.setClientID(session.getClientID());
			withdraw.setAmount(amount);
			withdraw.setTime(time);

			Movement expectedWithdraw = new Movement(withdrawID);
			expectedWithdraw.setClientID(session.getClientID());
			expectedWithdraw.setAmount(amount);
			expectedWithdraw.setReference(reference);
			expectedWithdraw.setTime(time);
			expectedWithdraw.setType(Movement.Type.WITHDRAW);
			MovementResp expectedWithdrawResp = new MovementResp(expectedWithdraw, MovementResp.SessionStatus.OK);

			MovementDAO expectedWithdrawDAO = new MovementDAO(withdrawID);
			expectedWithdrawDAO.setClientID(session.getClientID());
			expectedWithdrawDAO.setClientID(session.getClientID());
			expectedWithdrawDAO.setAmount(amount);
			expectedWithdrawDAO.setReference(reference);
			expectedWithdrawDAO.setTime(time);
			expectedWithdrawDAO.setType(MovementDAO.Type.WITHDRAW);

			when(sessionsRepository.findById(sessionUUID)).thenReturn(Optional.of(sessionDAO));
			when(transactionsRepository.save(withdrawDAO)).thenReturn(expectedWithdrawDAO);

			MovementResp obtainedWithdrawResp = service.doWithdraw(withdraw, sessionUUID);

			assertThat(obtainedWithdrawResp).isEqualTo(expectedWithdrawResp);
			verify(sessionsRepository, only()).findById(sessionUUID);
			verify(transactionsRepository, only()).save(withdrawDAO);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------- shouldNotWithdrawBecauseInvalidClient
	/**
	 * Performs a not valid withdraw (invalid client).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotWithdrawBecauseInvalidClient ()
	{
		/*
		try
		{
			long clientID = 10;
			Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.OK);
		
			Withdraw withdraw = new Withdraw();
			withdraw.setClientID(session.getClientID());
		
			Withdraw expectedWithdraw = new Withdraw();
			WithdrawResp expectedWithdrawResp = new WithdrawResp(expectedWithdraw, WithdrawResp.SessionStatus.OK);
		
			//when(sessionsRepository.get(session.getSessionID())).thenReturn(session);
			when(transactionsRepository.add(withdraw)).thenReturn(expectedWithdraw);
		
			WithdrawResp obtainedWithdrawResp = service.doWithdraw(withdraw, session.getSessionID());
		
			assertThat(obtainedWithdrawResp).isEqualTo(expectedWithdrawResp);
			//verify(sessionsRepository, only()).get(session.getSessionID());
			verify(transactionsRepository, only()).add(withdraw);
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
		*/
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------- shouldNotWithdrawBecauseExpiredSession
	/**
	 * Performs a not valid withdraw (expired session).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotWithdrawBecauseExpiredSession ()
	{
		try
		{
			long clientID = 10;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			UUID sessionUUID = UUID.fromString(sessionID);

			BigDecimal amount = new BigDecimal(834.54);
			Date time = new Date(1547645112000L); // 2019-01-16T09:25:12.000-04:00

			Date sessionExpiredDate = new Date(new Date().getTime() - 10000L);

			Session session = new Session(sessionID);
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.OK);

			SignDAO customer = new SignDAO();

			SessionDAO sessionDAO = new SessionDAO(sessionUUID);
			sessionDAO.setTime(sessionExpiredDate);
			sessionDAO.setCustomer(customer);

			Movement withdraw = new Movement();
			withdraw.setClientID(session.getClientID());
			withdraw.setAmount(amount);
			withdraw.setTime(time);

			Movement expectedWithdraw = new Movement();
			MovementResp expectedWithdrawResp = new MovementResp(expectedWithdraw,
					MovementResp.SessionStatus.EXPIRED);

			when(sessionsRepository.findById(sessionUUID)).thenReturn(Optional.of(sessionDAO));

			MovementResp obtainedWithdrawResp = service.doWithdraw(withdraw, sessionUUID);

			assertThat(obtainedWithdrawResp).isEqualTo(expectedWithdrawResp);
			verify(sessionsRepository, only()).findById(sessionUUID);
			verifyZeroInteractions(transactionsRepository);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------- shouldNotWithdrawBecauseInvalidSession
	/**
	 * Performs a not valid withdraw (invalid session - does not exist).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotWithdrawBecauseInvalidSession ()
	{
		try
		{
			long clientID = 10;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			UUID sessionUUID = UUID.fromString(sessionID);

			BigDecimal amount = new BigDecimal(834.54);
			Date time = new Date(1547645112000L); // 2019-01-16T09:25:12.000-04:00

			Movement withdraw = new Movement();
			withdraw.setClientID(clientID);
			withdraw.setAmount(amount);
			withdraw.setTime(time);

			Movement expectedWithdraw = new Movement();
			MovementResp expectedWithdrawResp = new MovementResp(expectedWithdraw,
					MovementResp.SessionStatus.DOES_NOT_EXIST);

			when(sessionsRepository.findById(sessionUUID)).thenReturn(Optional.empty());

			MovementResp obtainedWithdrawResp = service.doDeposit(withdraw, sessionUUID);

			assertThat(obtainedWithdrawResp).isEqualTo(expectedWithdrawResp);
			verify(sessionsRepository, only()).findById(sessionUUID);
			verifyZeroInteractions(transactionsRepository);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
	}
}
