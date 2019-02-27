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
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.AccountStatementResp;
import com.twinero.jtasks.nm.simplebanking.utils.Util;
import com.twinero.jtasks.nm.simplebanking.web.SimpleBankingController;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

		AccountStatement balance = new AccountStatement();
		AccountStatementResp accountBalanceResp = new AccountStatementResp(balance, AccountStatementResp.Status.OK);

		when(service.getAccountStatement(session.getClientID(), session.getSessionID())).thenReturn(accountBalanceResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountStatements/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(Util.asJsonString(accountBalanceResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountStatements/accountStatement"))
				.andReturn();
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
				.andExpect(content().string(containsString(Util.asJsonString(statementResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountStatements/expiredSession"))
				.andReturn();
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
				.andExpect(content().string(containsString(Util.asJsonString(statementResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountStatements/sessionDoesNotExist"))
				.andReturn();
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
				.andExpect(content().string(containsString(Util.asJsonString(statementResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountStatements/notAccountStatementServerError"))
				.andReturn();
	}
}
