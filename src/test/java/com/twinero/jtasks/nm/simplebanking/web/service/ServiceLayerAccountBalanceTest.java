package com.twinero.jtasks.nm.simplebanking.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.twinero.jtasks.nm.simplebanking.repository.MovementsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.SessionsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.StatementsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.MovementDAO;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SessionDAO;
import com.twinero.jtasks.nm.simplebanking.repository.beans.StatementDAO;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.Balance;
import com.twinero.jtasks.nm.simplebanking.service.beans.BalanceResp;

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
	private StatementsRepository statementsRepository;

	@Autowired
	@MockBean
	private MovementsRepository movementsRepository;

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
			UUID sessionUUID = UUID.fromString(sessionID);

			Date balanceDate = new Date(); // 2019-01-16T09:25:12.000-04:00
			Date sessionExpiredDate = new Date(new Date().getTime() + 10000);

			double statementFinalAmount = 4000.00;
			
			long movementID_01 = 2277;
			BigDecimal amount_01 = new BigDecimal(700.00);
			BigDecimal tax_01 = new BigDecimal(0.0);
			String reference_01 = "88776655";
			Date time_01 = new Date(1547645112000L); // 2019-01-16T09:25:12.000-04:00

			long movementID_02 = 2278;
			BigDecimal amount_02 = new BigDecimal(400.00);
			BigDecimal tax_02 = new BigDecimal(0.10);
			String reference_02 = "11006633";
			Date time_02 = new Date(1547645130000L); // 2019-01-16T09:25:20.000-04:00

			BigDecimal available = new BigDecimal(4299.90);
			available = available.setScale(2, BigDecimal.ROUND_UP);
			BigDecimal blocked = new BigDecimal(0);
			BigDecimal deferred = new BigDecimal(0);
			BigDecimal total = new BigDecimal(4299.90);
			total = total.setScale(2, BigDecimal.ROUND_UP);

			Balance expectedAccountBalance = new Balance();
			expectedAccountBalance.setClientID(clientID);
			expectedAccountBalance.setDate(balanceDate);
			expectedAccountBalance.setAvailable(available);
			expectedAccountBalance.setBlocked(blocked);
			expectedAccountBalance.setDeferred(deferred);
			expectedAccountBalance.setTotal(total);

			BalanceResp expectedAccountBalanceResp = new BalanceResp(expectedAccountBalance,
					BalanceResp.SessionStatus.OK);

			SessionDAO expectedSessionDAO = new SessionDAO(sessionUUID);
			expectedSessionDAO.setTime(sessionExpiredDate);

			Calendar cal = new GregorianCalendar();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) - 1;
			if (month == -1)
			{
				month = 11;
				year--;
			}

			cal = new GregorianCalendar(year, month, 1, 0, 0, 0);
			Date since = cal.getTime();

			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal = new GregorianCalendar(year, month, lastDay, 23, 59, 59);
			cal.set(Calendar.MILLISECOND, 999);

			Date until = cal.getTime();
			
			MovementDAO expectedMovementDAO_01 = new MovementDAO(movementID_01);
			expectedMovementDAO_01.setAmount(amount_01);
			expectedMovementDAO_01.setClientID(clientID);
			expectedMovementDAO_01.setDescription("Deposit_01");
			expectedMovementDAO_01.setReference(reference_01);
			expectedMovementDAO_01.setTax(tax_01);
			expectedMovementDAO_01.setTime(time_01);
			expectedMovementDAO_01.setType(MovementDAO.Type.DEPOSIT);

			MovementDAO expectedMovementDAO_02 = new MovementDAO(movementID_02);
			expectedMovementDAO_02.setAmount(amount_02);
			expectedMovementDAO_02.setClientID(clientID);
			expectedMovementDAO_02.setDescription("Withdraw_02");
			expectedMovementDAO_02.setReference(reference_02);
			expectedMovementDAO_02.setTax(tax_02);
			expectedMovementDAO_02.setTime(time_02);
			expectedMovementDAO_02.setType(MovementDAO.Type.WITHDRAW);
			
			List<MovementDAO> expectedMovementsDAO = new ArrayList<>();
			expectedMovementsDAO.add(expectedMovementDAO_01);
			expectedMovementsDAO.add(expectedMovementDAO_02);

			StatementDAO example = new StatementDAO(clientID, year, month);
			StatementDAO expectedStatementDAO = new StatementDAO();
			expectedStatementDAO.setFinalAmount(new BigDecimal(statementFinalAmount));

			when(sessionsRepository.findById(sessionUUID)).thenReturn(Optional.of(expectedSessionDAO));
			when(statementsRepository.findOne(Example.of(example))).thenReturn(Optional.of(expectedStatementDAO));

			when(movementsRepository.findByCustomerAndTimeBetween(clientID, since, until))
					.thenReturn(expectedMovementsDAO);

			BalanceResp obtainedAccountBalanceResp = service.getBalance(clientID, sessionUUID);
			obtainedAccountBalanceResp.getBalance().setDate(balanceDate);

			assertThat(obtainedAccountBalanceResp).isEqualTo(expectedAccountBalanceResp);
			verify(sessionsRepository, only()).findById(sessionUUID);
			verify(statementsRepository, only()).findOne(Example.of(example));
			verify(movementsRepository, only()).findByCustomerAndTimeBetween(clientID, since, until);
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
	// -------------------------------------------------------------- shouldReturnAccountBalanceWithoutPreviousStatement
	/**
	 * Gets a valid account balance without previous statement.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldReturnAccountBalanceWithoutPreviousStatement ()
	{
		try
		{
			long clientID = 10;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			UUID sessionUUID = UUID.fromString(sessionID);

			Date balanceDate = new Date(); // 2019-01-16T09:25:12.000-04:00
			Date sessionExpiredDate = new Date(new Date().getTime() + 10000);

			long movementID_01 = 2277;
			BigDecimal amount_01 = new BigDecimal(700.00);
			BigDecimal tax_01 = new BigDecimal(0.0);
			String reference_01 = "88776655";
			Date time_01 = new Date(1547645112000L); // 2019-01-16T09:25:12.000-04:00

			long movementID_02 = 2278;
			BigDecimal amount_02 = new BigDecimal(400.00);
			BigDecimal tax_02 = new BigDecimal(0.10);
			String reference_02 = "11006633";
			Date time_02 = new Date(1547645130000L); // 2019-01-16T09:25:20.000-04:00

			BigDecimal available = new BigDecimal(299.90);
			available = available.setScale(2, BigDecimal.ROUND_UP);
			BigDecimal blocked = new BigDecimal(0);
			BigDecimal deferred = new BigDecimal(0);
			BigDecimal total = new BigDecimal(299.90);
			total = total.setScale(2, BigDecimal.ROUND_UP);

			Balance expectedAccountBalance = new Balance();
			expectedAccountBalance.setClientID(clientID);
			expectedAccountBalance.setDate(balanceDate);
			expectedAccountBalance.setAvailable(available);
			expectedAccountBalance.setBlocked(blocked);
			expectedAccountBalance.setDeferred(deferred);
			expectedAccountBalance.setTotal(total);

			BalanceResp expectedAccountBalanceResp = new BalanceResp(expectedAccountBalance,
					BalanceResp.SessionStatus.OK);

			SessionDAO expectedSessionDAO = new SessionDAO(sessionUUID);
			expectedSessionDAO.setTime(sessionExpiredDate);

			Calendar cal = new GregorianCalendar();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) - 1;
			if (month == -1)
			{
				month = 11;
				year--;
			}

			cal = new GregorianCalendar(year, month, 1, 0, 0, 0);
			Date since = cal.getTime();

			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal = new GregorianCalendar(year, month, lastDay, 23, 59, 59);
			cal.set(Calendar.MILLISECOND, 999);

			Date until = cal.getTime();
			
			MovementDAO expectedMovementDAO_01 = new MovementDAO(movementID_01);
			expectedMovementDAO_01.setAmount(amount_01);
			expectedMovementDAO_01.setClientID(clientID);
			expectedMovementDAO_01.setDescription("Deposit_01");
			expectedMovementDAO_01.setReference(reference_01);
			expectedMovementDAO_01.setTax(tax_01);
			expectedMovementDAO_01.setTime(time_01);
			expectedMovementDAO_01.setType(MovementDAO.Type.DEPOSIT);

			MovementDAO expectedMovementDAO_02 = new MovementDAO(movementID_02);
			expectedMovementDAO_02.setAmount(amount_02);
			expectedMovementDAO_02.setClientID(clientID);
			expectedMovementDAO_02.setDescription("Withdraw_02");
			expectedMovementDAO_02.setReference(reference_02);
			expectedMovementDAO_02.setTax(tax_02);
			expectedMovementDAO_02.setTime(time_02);
			expectedMovementDAO_02.setType(MovementDAO.Type.WITHDRAW);
			
			List<MovementDAO> expectedMovementsDAO = new ArrayList<>();
			expectedMovementsDAO.add(expectedMovementDAO_01);
			expectedMovementsDAO.add(expectedMovementDAO_02);

			StatementDAO example = new StatementDAO(clientID, year, month);

			when(sessionsRepository.findById(sessionUUID)).thenReturn(Optional.of(expectedSessionDAO));
			when(statementsRepository.findOne(Example.of(example))).thenReturn(Optional.empty());

			when(movementsRepository.findByCustomerAndTimeBetween(clientID, since, until))
					.thenReturn(expectedMovementsDAO);

			BalanceResp obtainedAccountBalanceResp = service.getBalance(clientID, sessionUUID);
			obtainedAccountBalanceResp.getBalance().setDate(balanceDate);

			assertThat(obtainedAccountBalanceResp).isEqualTo(expectedAccountBalanceResp);
			verify(sessionsRepository, only()).findById(sessionUUID);
			verify(statementsRepository, only()).findOne(Example.of(example));
			verify(movementsRepository, only()).findByCustomerAndTimeBetween(clientID, since, until);
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
			UUID sessionUUID = UUID.fromString(sessionID);
			
			Date sessionExpiredDate = new Date(new Date().getTime() - 10000);
			
			Balance expectedAccountBalance = new Balance();
			BalanceResp expectedAccountBalanceResp = new BalanceResp(expectedAccountBalance,
					BalanceResp.SessionStatus.EXPIRED);
	
			SessionDAO expectedSessionDAO = new SessionDAO(sessionUUID);
			expectedSessionDAO.setTime(sessionExpiredDate);
			
			when(sessionsRepository.findById(sessionUUID)).thenReturn(Optional.of(expectedSessionDAO));
	
			BalanceResp obtainedAccountBalanceResp = service.getBalance(clientID, sessionUUID);
	
			assertThat(obtainedAccountBalanceResp).isEqualTo(expectedAccountBalanceResp);
			verify(sessionsRepository, only()).findById(sessionUUID);
			verifyZeroInteractions(statementsRepository);
			verifyZeroInteractions(movementsRepository);
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
			UUID sessionUUID = UUID.fromString(sessionID);
			
			BalanceResp expectedAccountBalanceResp = new BalanceResp(new Balance(),
					BalanceResp.SessionStatus.DOES_NOT_EXIST);
	
			when(sessionsRepository.findById(sessionUUID)).thenReturn(Optional.empty());
	
			BalanceResp obtainedAccountBalanceResp = service.getBalance(clientID, sessionUUID);
	
			assertThat(obtainedAccountBalanceResp).isEqualTo(expectedAccountBalanceResp);
			verify(sessionsRepository, only()).findById(sessionUUID);
			verifyZeroInteractions(statementsRepository);
			verifyZeroInteractions(movementsRepository);
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
