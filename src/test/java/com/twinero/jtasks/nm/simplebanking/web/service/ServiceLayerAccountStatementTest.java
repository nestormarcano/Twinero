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

import com.twinero.jtasks.nm.simplebanking.repository.AccountStatementsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.SessionsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountStatement;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.AccountStatementResp;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceLayerAccountStatementTest
{
	@Autowired
	private SimpleBankService service;
	
	@Autowired
	@MockBean
	private SessionsRepository sessionsRepository;

	@Autowired
	@MockBean
	private AccountStatementsRepository accountStatementsRepository;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------ shouldReturnAccountStatement
	/**
	 * Gets a valid account statement.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldReturnAccountStatement ()
	{
		try
		{
			long clientID = 10;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			
			Session session = new Session(sessionID);
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.OK);

			AccountStatement expectedAccountStatement = new AccountStatement();
			AccountStatementResp expectedAccountStatementResp = new AccountStatementResp(expectedAccountStatement,
					AccountStatementResp.Status.OK);

			when(sessionsRepository.get(sessionID)).thenReturn(session);
			when(accountStatementsRepository.get(clientID)).thenReturn(expectedAccountStatement);

			AccountStatementResp obtainedAccountStatementResp = service.getAccountStatement(clientID, sessionID);

			assertThat(obtainedAccountStatementResp).isEqualTo(expectedAccountStatementResp);
			verify(sessionsRepository, only()).get(sessionID);
			verify(accountStatementsRepository, only()).get(clientID);
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
	// --------------------------------------------------------- shouldNotReturnTheAccountStatementBecauseExpiredSession
	/**
	 * Doesn't get valid account statement (expired session).
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotReturnTheAccountStatementBecauseExpiredSession ()
	{
		try
		{
			long clientID = 10;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			
			Session session = new Session(sessionID);
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.EXPIRED);

			AccountStatement expectedAccountStatement = new AccountStatement();
			AccountStatementResp expectedAccountStatementResp = new AccountStatementResp(expectedAccountStatement,
					AccountStatementResp.Status.SESSION_EXPIRED);

			when(sessionsRepository.get(sessionID)).thenReturn(session);

			AccountStatementResp obtainedAccountStatementResp = service.getAccountStatement(clientID, sessionID);

			assertThat(obtainedAccountStatementResp).isEqualTo(expectedAccountStatementResp);
			verify(sessionsRepository, only()).get(sessionID);
			verifyNoMoreInteractions(accountStatementsRepository);
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
	// ---------------------------------------------------- shouldNotReturnTheAccountStatementBecauseSessionDoesNotExist
	/**
	 * Doesn't get valid account statement (session doesn't exist).
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotReturnTheAccountStatementBecauseSessionDoesNotExist ()
	{
		try
		{
			long clientID = 10;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			
			Session session = new Session(sessionID);
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.NOT_EXISTS);

			AccountStatement expectedAccountStatement = new AccountStatement();
			AccountStatementResp expectedAccountStatementResp = new AccountStatementResp(expectedAccountStatement,
					AccountStatementResp.Status.SESSION_DOES_NOT_EXIST);

			when(sessionsRepository.get(sessionID)).thenReturn(session);

			AccountStatementResp obtainedAccountStatementResp = service.getAccountStatement(clientID, sessionID);

			assertThat(obtainedAccountStatementResp).isEqualTo(expectedAccountStatementResp);
			verify(sessionsRepository, only()).get(sessionID);
			verifyNoMoreInteractions(accountStatementsRepository);
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
	// ------------------------------------------------------------ shouldNotReturnTheAccountStatementBecauseServerError
	/**
	 * Doesn't get valid account statement (server error).
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotReturnTheAccountStatementBecauseServerError ()
	{
		try
		{
			long clientID = 10;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			
			Session session = new Session(sessionID);
			session.setClientID(clientID);
			session.setSessionStatus(Session.Status.OK);

			when(sessionsRepository.get(sessionID)).thenReturn(session);
			when(accountStatementsRepository.get(clientID)).thenThrow(SimpleBankServiceException.class);

			try
			{
				service.getAccountStatement(clientID, sessionID);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(sessionsRepository, only()).get(sessionID);
				clearInvocations(sessionsRepository);
				
				verify(accountStatementsRepository, only()).get(clientID);
				clearInvocations(accountStatementsRepository);
			}

			verifyNoMoreInteractions(sessionsRepository);
			verifyNoMoreInteractions(accountStatementsRepository);
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
