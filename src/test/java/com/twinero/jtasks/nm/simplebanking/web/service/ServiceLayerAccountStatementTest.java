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

import com.twinero.jtasks.nm.simplebanking.beans.AccountStatement;
import com.twinero.jtasks.nm.simplebanking.beans.AccountStatementResp;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.repository.SimpleBankRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SesionStatus;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceLayerAccountStatementTest
{
	@Autowired
	private SimpleBankService service;

	@Autowired
	@MockBean
	private SimpleBankRepository repository;

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

			AccountStatement statement = new AccountStatement();
			AccountStatementResp expectedAccountStatement = new AccountStatementResp(statement,
					AccountStatementResp.Status.OK);

			when(repository.verifySession(sessionID)).thenReturn(SesionStatus.OK);
			when(repository.getAccountStatement(clientID)).thenReturn(expectedAccountStatement);

			AccountStatementResp obtainedAccountStatement = service.getAccountStatement(clientID, sessionID);

			assertThat(obtainedAccountStatement).isEqualTo(expectedAccountStatement);
			verify(repository, times(1)).verifySession(sessionID);
			verify(repository, times(1)).getAccountStatement(clientID);
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

			AccountStatement statement = new AccountStatement();
			AccountStatementResp expectedAccountStatement = new AccountStatementResp(statement,
					AccountStatementResp.Status.SESSION_EXPIRED);

			when(repository.verifySession(sessionID)).thenReturn(SesionStatus.EXPIRED);

			AccountStatementResp obtainedAccountStatement = service.getAccountStatement(clientID, sessionID);

			assertThat(obtainedAccountStatement).isEqualTo(expectedAccountStatement);
			verify(repository, only()).verifySession(sessionID);
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

			AccountStatement statement = new AccountStatement();
			AccountStatementResp expectedAccountStatement = new AccountStatementResp(statement,
					AccountStatementResp.Status.SESSION_DOES_NOT_EXIST);

			when(repository.verifySession(sessionID)).thenReturn(SesionStatus.NOT_EXISTS);

			AccountStatementResp obtainedAccountStatement = service.getAccountStatement(clientID, sessionID);

			assertThat(obtainedAccountStatement).isEqualTo(expectedAccountStatement);
			verify(repository, only()).verifySession(sessionID);
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

			when(repository.verifySession(sessionID)).thenReturn(SesionStatus.OK);
			when(repository.getAccountStatement(clientID)).thenThrow(SimpleBankServiceException.class);

			try
			{
				service.getAccountStatement(clientID, sessionID);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(repository, times(1)).verifySession(sessionID);
				verify(repository, times(1)).getAccountStatement(clientID);
				verifyNoMoreInteractions(repository);
				clearInvocations(repository);
			}

			verify(repository, times(0)).verifySession(sessionID);
			verify(repository, times(0)).getAccountStatement(clientID);
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
