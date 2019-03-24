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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import com.twinero.jtasks.nm.simplebanking.service.beans.Statement;
import com.twinero.jtasks.nm.simplebanking.service.beans.StatementResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.Movement;

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
	private StatementsRepository statementsRepository;

	@Autowired
	@MockBean
	private MovementsRepository movementsRepository;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------ shouldReturnAccountStatement
	/**
	 * Gets a valid account statement.
	 * Runs with: mvn -Dtest=ServiceLayerAccountStatementTest#shouldReturnAccountStatement test
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldReturnAccountStatement ()
	{
		try
		{
			long clientID = 10;
			long statementID = 567;
			String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			UUID uuidSession = UUID.fromString(sessionID);
			Date sessionExpiredDate = new Date(new Date().getTime() + 10000L);

			String accountNumber = "12345678901234567890";

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

			SessionDAO foundSessionDAO = new SessionDAO(uuidSession);
			foundSessionDAO.setTime(sessionExpiredDate);
			Optional<SessionDAO> foundOptionalSessionDAO = Optional.of(foundSessionDAO);

			StatementDAO toFindStatement = new StatementDAO(clientID, 2019, 1);

			StatementDAO foundStatementDAO = new StatementDAO(statementID);
			foundStatementDAO.setAccountNumber(accountNumber);
			foundStatementDAO.setFinalAmount(new BigDecimal(5483.83));
			foundStatementDAO.setInitialAmount(new BigDecimal(3548.45));
			foundStatementDAO.setYear(2019);
			foundStatementDAO.setMonth(1);

			long movementID_01 = 2277;
			BigDecimal amount_01 = new BigDecimal(3467.25);
			BigDecimal tax_01 = new BigDecimal(0.0);
			String reference_01 = "88776655";
			String description_01 = "Deposit 01";
			Date time_01 = new Date(1547645112000L); // 2019-01-16T09:25:12.000-04:00

			Movement expectedMovement_01 = new Movement(movementID_01);
			expectedMovement_01.setAmount(amount_01);
			expectedMovement_01.setClientID(clientID);
			expectedMovement_01.setDescription(description_01);
			expectedMovement_01.setReference(reference_01);
			expectedMovement_01.setTax(tax_01);
			expectedMovement_01.setTime(time_01);
			expectedMovement_01.setType(Movement.Type.DEPOSIT);

			long movementID_02 = 2278;
			BigDecimal amount_02 = new BigDecimal(730.50);
			BigDecimal tax_02 = new BigDecimal(0.10);
			String reference_02 = "11006633";
			String description_02 = "Withdraw 02";
			Date time_02 = new Date(1547645130000L); // 2019-01-16T09:25:20.000-04:00

			Movement expectedMovement_02 = new Movement(movementID_02);
			expectedMovement_02.setAmount(amount_02);
			expectedMovement_02.setClientID(clientID);
			expectedMovement_02.setDescription(description_02);
			expectedMovement_02.setReference(reference_02);
			expectedMovement_02.setTax(tax_02);
			expectedMovement_02.setTime(time_02);
			expectedMovement_02.setType(Movement.Type.WITHDRAW);

			Set<Movement> expectedMovements = new HashSet<>();
			expectedMovements.add(expectedMovement_01);
			expectedMovements.add(expectedMovement_02);

			Statement expectedStatement = new Statement(statementID);
			expectedStatement.setAccountNumber(accountNumber);
			expectedStatement.setClientID(clientID);
			expectedStatement.setSince(since);
			expectedStatement.setUntil(until);
			expectedStatement.setMovements(expectedMovements);

			StatementResp expectedAccountStatementResp = new StatementResp(expectedStatement,
					StatementResp.SessionStatus.OK);

			MovementDAO expectedMovementDAO_01 = new MovementDAO(movementID_01);
			expectedMovementDAO_01.setAmount(amount_01);
			expectedMovementDAO_01.setClientID(clientID);
			expectedMovementDAO_01.setDescription(description_01);
			expectedMovementDAO_01.setReference(reference_01);
			expectedMovementDAO_01.setTax(tax_01);
			expectedMovementDAO_01.setTime(time_01);
			expectedMovementDAO_01.setType(MovementDAO.Type.DEPOSIT);

			MovementDAO expectedMovementDAO_02 = new MovementDAO(movementID_02);
			expectedMovementDAO_02.setAmount(amount_02);
			expectedMovementDAO_02.setClientID(clientID);
			expectedMovementDAO_02.setDescription(description_02);
			expectedMovementDAO_02.setReference(reference_02);
			expectedMovementDAO_02.setTax(tax_02);
			expectedMovementDAO_02.setTime(time_02);
			expectedMovementDAO_02.setType(MovementDAO.Type.WITHDRAW);

			List<MovementDAO> expectedMovementsDAO = new ArrayList<>();
			expectedMovementsDAO.add(expectedMovementDAO_01);
			expectedMovementsDAO.add(expectedMovementDAO_02);

			when(sessionsRepository.findById(uuidSession)).thenReturn(foundOptionalSessionDAO);
			when(statementsRepository.findOne(Example.of(toFindStatement)))
					.thenReturn(Optional.of(foundStatementDAO));
			when(movementsRepository.findByCustomerAndTimeBetween(clientID, since, until))
					.thenReturn(expectedMovementsDAO);

			StatementResp obtainedAccountStatementResp = service.getStatement(clientID, uuidSession);

			assertThat(obtainedAccountStatementResp).isEqualTo(expectedAccountStatementResp);
			verify(sessionsRepository, only()).findById(uuidSession);
			verify(statementsRepository, only()).findOne(Example.of(toFindStatement));
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
			UUID uuidSession = UUID.fromString(sessionID);
			Date sessionExpiredDate = new Date(new Date().getTime() - 10000L);

			SessionDAO foundSessionDAO = new SessionDAO(uuidSession);
			foundSessionDAO.setTime(sessionExpiredDate);
			Optional<SessionDAO> foundOptionalSessionDAO = Optional.of(foundSessionDAO);

			StatementResp expectedAccountStatementResp = new StatementResp(null,
					StatementResp.SessionStatus.EXPIRED);

			when(sessionsRepository.findById(uuidSession)).thenReturn(foundOptionalSessionDAO);

			StatementResp obtainedAccountStatementResp = service.getStatement(clientID, uuidSession);

			assertThat(obtainedAccountStatementResp).isEqualTo(expectedAccountStatementResp);

			verify(sessionsRepository, only()).findById(uuidSession);
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
			UUID uuidSession = UUID.fromString(sessionID);

			StatementResp expectedAccountStatementResp = new StatementResp(null,
					StatementResp.SessionStatus.DOES_NOT_EXIST);

			when(sessionsRepository.findById(uuidSession)).thenReturn(Optional.empty());

			StatementResp obtainedAccountStatementResp = service.getStatement(clientID, uuidSession);

			assertThat(obtainedAccountStatementResp).isEqualTo(expectedAccountStatementResp);

			verify(sessionsRepository, only()).findById(uuidSession);
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
