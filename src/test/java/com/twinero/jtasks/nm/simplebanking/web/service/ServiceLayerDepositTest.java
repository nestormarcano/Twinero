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

import com.twinero.jtasks.nm.simplebanking.repository.MovementsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.SessionsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.SignupsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SessionDAO;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SignDAO;
import com.twinero.jtasks.nm.simplebanking.repository.beans.MovementDAO;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.MovementResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.Session;
import com.twinero.jtasks.nm.simplebanking.service.beans.Movement;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceLayerDepositTest
{
	@Autowired
	private SimpleBankService service;
	
	@Autowired
	@MockBean
	private SignupsRepository signupsRepository;
	
	@Autowired
	@MockBean
	private SessionsRepository sessionsRepository;

	@Autowired
	@MockBean
	private MovementsRepository transactionsRepository;
	
	

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
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			UUID sessionUUID = UUID.fromString(sessionID);
			long depositID = 123;
			
			BigDecimal amount = new BigDecimal(834.54);
			String reference = "276534982";
			Date time = new Date(1547645112000L); // 2019-01-16T09:25:12.000-04:00
			
			Date sessionExpiredDate = new Date(new Date().getTime() + 10000L);
			
			Session session = new Session(sessionID);
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.OK);
			
			SignDAO customer = new SignDAO();
			
			SessionDAO sessionDAO = new SessionDAO(sessionUUID);
			sessionDAO.setTime(sessionExpiredDate);
			sessionDAO.setCustomer(customer);

			MovementDAO depositDAO = new MovementDAO();
			depositDAO.setClientID(session.getClientID());
			depositDAO.setAmount(amount);
			depositDAO.setTime(time);
			depositDAO.setType(MovementDAO.Type.DEPOSIT);
			
			Movement deposit = new Movement();
			deposit.setClientID(session.getClientID());
			deposit.setAmount(amount);
			deposit.setTime(time);

			Movement expectedDeposit = new Movement(depositID);
			expectedDeposit.setClientID(session.getClientID());
			expectedDeposit.setAmount(amount);
			expectedDeposit.setReference(reference);
			expectedDeposit.setTime(time);
			expectedDeposit.setType(Movement.Type.DEPOSIT);
			MovementResp expectedDepositResp = new MovementResp(expectedDeposit, MovementResp.SessionStatus.OK);
			
			MovementDAO expectedDepositDAO = new MovementDAO(depositID);
			expectedDepositDAO.setClientID(session.getClientID());
			expectedDepositDAO.setClientID(session.getClientID());
			expectedDepositDAO.setAmount(amount);
			expectedDepositDAO.setReference(reference);
			expectedDepositDAO.setTime(time);
			expectedDepositDAO.setType(MovementDAO.Type.DEPOSIT);

			when(sessionsRepository.findById(sessionUUID)).thenReturn(Optional.of(sessionDAO));
			when(transactionsRepository.save(depositDAO)).thenReturn(expectedDepositDAO);

			MovementResp obtainedDepositResp = service.doDeposit(deposit, sessionUUID);

			assertThat(obtainedDepositResp).isEqualTo(expectedDepositResp);
			verify(sessionsRepository, only()).findById(sessionUUID);
			verify(transactionsRepository, only()).save(depositDAO);
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
	// ---------------------------------------------------------------------------- shouldNotDepositBecauseInvalidClient
	/* *
	 * Performs a not valid deposit (invalid client).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	/*
	@Test
	public void shouldNotDepositBecauseInvalidClient ()
	{
		try
		{
			long clientID = 10;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			UUID sessionUUID = UUID.fromString(sessionID);
			long depositID = 123;
			
			BigDecimal amount = new BigDecimal(834.54);
			String reference = "276534982";
			Date time = new Date(1547645112000L); // 2019-01-16T09:25:12.000-04:00
			
			Date sessionExpiredDate = new Date(new Date().getTime() + 10000L);
			
			Session session = new Session(sessionID);
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.OK);
			
			SignDAO customer = new SignDAO();
			
			SessionDAO expectedSessionDAO = new SessionDAO(sessionUUID);
			expectedSessionDAO.setTime(sessionExpiredDate);
			expectedSessionDAO.setCustomer(customer);

			MovementDAO depositDAO = new MovementDAO();
			depositDAO.setClientID(session.getClientID());
			depositDAO.setClientID(session.getClientID());
			depositDAO.setAmount(amount);
			depositDAO.setTime(time);
			depositDAO.setType(MovementDAO.Type.DEPOSIT);
			
			Movement deposit = new Movement();
			deposit.setClientID(session.getClientID());
			deposit.setAmount(amount);
			deposit.setTime(time);

			Movement expectedDeposit = new Movement(depositID);
			expectedDeposit.setClientID(session.getClientID());
			expectedDeposit.setAmount(amount);
			expectedDeposit.setReference(reference);
			expectedDeposit.setTime(time);
			MovementResp expectedDepositResp = new MovementResp(expectedDeposit, MovementResp.SessionStatus.OK);
			
			MovementDAO expectedDepositDAO = new MovementDAO(depositID);
			expectedDepositDAO.setClientID(session.getClientID());
			expectedDepositDAO.setClientID(session.getClientID());
			expectedDepositDAO.setAmount(amount);
			expectedDepositDAO.setReference(reference);
			expectedDepositDAO.setTime(time);
			expectedDepositDAO.setType(MovementDAO.Type.DEPOSIT);

			when(sessionsRepository.findById(sessionUUID)).thenReturn(Optional.of(expectedSessionDAO));
			when(signupsRepository.findById(clientID)).thenReturn(Optional.of(customer));

			MovementResp obtainedDepositResp = service.doDeposit(deposit, sessionUUID);

			assertThat(obtainedDepositResp).isEqualTo(expectedDepositResp);
			verify(sessionsRepository, only()).findById(sessionUUID);
			verify(signupsRepository, only()).findById(clientID);
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
	*/

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

			Movement deposit = new Movement();
			deposit.setClientID(session.getClientID());
			deposit.setAmount(amount);
			deposit.setTime(time);

			Movement expectedDeposit = new Movement();
			MovementResp expectedDepositResp = new MovementResp(expectedDeposit, MovementResp.SessionStatus.EXPIRED);
			
			when(sessionsRepository.findById(sessionUUID)).thenReturn(Optional.of(sessionDAO));

			MovementResp obtainedDepositResp = service.doDeposit(deposit, sessionUUID);

			assertThat(obtainedDepositResp).isEqualTo(expectedDepositResp);
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
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			UUID sessionUUID = UUID.fromString(sessionID);
			
			BigDecimal amount = new BigDecimal(834.54);
			Date time = new Date(1547645112000L); // 2019-01-16T09:25:12.000-04:00
			
			Movement deposit = new Movement();
			deposit.setClientID(clientID);
			deposit.setAmount(amount);
			deposit.setTime(time);

			Movement expectedDeposit = new Movement();
			MovementResp expectedDepositResp = new MovementResp(expectedDeposit, MovementResp.SessionStatus.DOES_NOT_EXIST);
			
			when(sessionsRepository.findById(sessionUUID)).thenReturn(Optional.empty());

			MovementResp obtainedDepositResp = service.doDeposit(deposit, sessionUUID);

			assertThat(obtainedDepositResp).isEqualTo(expectedDepositResp);
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
