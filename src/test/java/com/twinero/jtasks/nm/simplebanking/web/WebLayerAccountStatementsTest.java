package com.twinero.jtasks.nm.simplebanking.web;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;

import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountStatement;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Movement;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.AccountStatementResp;
import com.twinero.jtasks.nm.simplebanking.utils.Util;
import com.twinero.jtasks.nm.simplebanking.web.SimpleBankingController;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(SimpleBankingController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class WebLayerAccountStatementsTest
{
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SimpleBankService service;

	// ----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------- shouldReturnAnAccountStatement
	/**
	 * Gets the account statement.
	 * 
	 * @throws Exception Data Error.
	 */
	// ----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldReturnAnAccountStatement ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		List<Movement> expectedMovements = new ArrayList<Movement>();

		Movement expectedMovement_01 = new Movement(18534);
		expectedMovement_01.setDate(new Date(1547645112000L)); // 2019-01-16T09:25:12.741-04:00
		expectedMovement_01.setDeposit(new BigDecimal(2109.43));
		expectedMovement_01.setDescription("Branch deposit");
		expectedMovement_01.setReference("18702753");
		expectedMovement_01.setTax(new BigDecimal(0.01));

		Movement expectedMovement_02 = new Movement(908728);
		expectedMovement_02.setDate(new Date(1547857051000L)); // 2019-01-18T20:17:31.298-04:00
		expectedMovement_02.setWithdraw(new BigDecimal(187.50));
		expectedMovement_02.setDescription("Toy buy");
		expectedMovement_02.setReference("729601");
		expectedMovement_02.setTax(new BigDecimal(0.02));

		expectedMovements.add(expectedMovement_01);
		expectedMovements.add(expectedMovement_02);

		AccountStatement expectedAccountStatement = new AccountStatement(998);
		expectedAccountStatement.setAccountNumber("12345678901234567890");
		expectedAccountStatement.setClientID(clientID);
		expectedAccountStatement.setSince(new Date(1546315200000L)); // 2019-01-01T00:00:00.000-04:00
		expectedAccountStatement.setUntil(new Date(1546401599000L)); // 2019-01-31T23:59:59.999-04:00

		expectedAccountStatement.setMovements(expectedMovements);

		AccountStatementResp expectedAccountStatementResp = new AccountStatementResp(expectedAccountStatement,
				AccountStatementResp.Status.OK);

		when(service.getAccountStatement(session.getClientID(), session.getSessionID()))
				.thenReturn(expectedAccountStatementResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountStatements/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content()
						.string(
								containsString(Util.asJsonString(expectedAccountStatementResp, AccountStatement.DATE_FORMAT))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountStatements/accountStatement"))
				.andReturn();

		verify(service, only()).getAccountStatement(session.getClientID(), session.getSessionID());
	}

	// ----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------- shouldNotReturnTheAccountStatementBecauseExpiredSession
	/**
	 * Tries to get the account statement, but the session is expired.
	 * 
	 * @throws Exception Data Error.
	 */
	// ----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotReturnTheAccountStatementBecauseExpiredSession ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		AccountStatement statement = new AccountStatement();
		AccountStatementResp statementResp = new AccountStatementResp(statement,
				AccountStatementResp.Status.SESSION_EXPIRED);

		when(service.getAccountStatement(session.getClientID(), session.getSessionID())).thenReturn(statementResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountStatements/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(statementResp, AccountStatement.DATE_FORMAT))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountStatements/expiredSession"))
				.andReturn();

		verify(service, only()).getAccountStatement(session.getClientID(), session.getSessionID());
	}

	// ----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------- shouldNotReturnTheAccountStatementBecauseSessionDoesNotExist
	/**
	 * Tries to get the account statement, but the session doesn't exist.
	 * 
	 * @throws Exception Data Error.
	 */
	// ----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotReturnTheAccountStatementBecauseSessionDoesNotExist ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		AccountStatement statement = new AccountStatement();
		AccountStatementResp statementResp = new AccountStatementResp(statement,
				AccountStatementResp.Status.SESSION_DOES_NOT_EXIST);

		when(service.getAccountStatement(session.getClientID(), session.getSessionID())).thenReturn(statementResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountStatements/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(statementResp, AccountStatement.DATE_FORMAT))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountStatements/sessionDoesNotExist"))
				.andReturn();

		verify(service, only()).getAccountStatement(session.getClientID(), session.getSessionID());
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------ shouldNotReturnTheAccountStatementBecauseServerError
	/**
	 * Tries to get the account statement, but a server error occurs.
	 * @throws Exception Data Error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotReturnTheAccountStatementBecauseServerError ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		AccountStatement statement = new AccountStatement();
		AccountStatementResp statementResp = new AccountStatementResp(statement,
				AccountStatementResp.Status.SERVER_ERROR);

		when(service.getAccountStatement(session.getClientID(), session.getSessionID()))
				.thenThrow(SimpleBankServiceException.class);

		this.mockMvc
				.perform(get("/simpleBanking/accountStatements/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content().string(containsString(Util.asJsonString(statementResp, AccountStatement.DATE_FORMAT))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountStatements/notAccountStatementServerError"))
				.andReturn();

		verify(service, only()).getAccountStatement(session.getClientID(), session.getSessionID());
	}
}
