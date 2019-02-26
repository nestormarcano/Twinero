package com.twinero.jtasks.nm.simplebanking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.twinero.jtasks.nm.simplebanking.beans.AccountBalance;
import com.twinero.jtasks.nm.simplebanking.beans.AccountBalanceResp;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.repository.SimpleBankRepository;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountBalanceServiceTest
{
	@Autowired
	private SimpleBankService service;

	@Autowired
	@MockBean
	private SimpleBankRepository repository;

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

			AccountBalance balance = new AccountBalance();
			AccountBalanceResp expectedAccountBalance = new AccountBalanceResp(balance,
					AccountBalanceResp.Status.OK);

			when(repository.getAccountBalance(clientID, sessionID)).thenReturn(expectedAccountBalance);

			AccountBalanceResp obtainedAccountBalance = service.getAccountBalance(clientID, sessionID);

			assertThat(obtainedAccountBalance).isEqualTo(expectedAccountBalance);
			verify(repository, only()).getAccountBalance(clientID, sessionID);
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

			AccountBalance balance = new AccountBalance();
			AccountBalanceResp expectedAccountBalance = new AccountBalanceResp(balance,
					AccountBalanceResp.Status.SESSION_EXPIRED);

			when(repository.getAccountBalance(clientID, sessionID)).thenReturn(expectedAccountBalance);

			AccountBalanceResp obtainedAccountBalance = service.getAccountBalance(clientID, sessionID);

			assertThat(obtainedAccountBalance).isEqualTo(expectedAccountBalance);
			verify(repository, only()).getAccountBalance(clientID, sessionID);
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

			AccountBalance balance = new AccountBalance();
			AccountBalanceResp expectedAccountBalance = new AccountBalanceResp(balance,
					AccountBalanceResp.Status.SESSION_DOES_NOT_EXISTS);

			when(repository.getAccountBalance(clientID, sessionID)).thenReturn(expectedAccountBalance);

			AccountBalanceResp obtainedAccountBalance = service.getAccountBalance(clientID, sessionID);

			assertThat(obtainedAccountBalance).isEqualTo(expectedAccountBalance);
			verify(repository, only()).getAccountBalance(clientID, sessionID);
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

			when(repository.getAccountBalance(clientID, sessionID)).thenThrow(SimpleBankServiceException.class);

			try
			{
				service.getAccountBalance(clientID, sessionID);
			}
			catch (SimpleBankServiceException ex)
			{
				verify(repository, only()).getAccountBalance(clientID, sessionID);
				clearInvocations(repository);
			}

			verify(repository, times(0)).getAccountBalance(clientID, sessionID);
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
