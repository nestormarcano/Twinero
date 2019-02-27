package com.twinero.jtasks.nm.simplebanking.web;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;

import com.twinero.jtasks.nm.simplebanking.beans.AccountBalance;
import com.twinero.jtasks.nm.simplebanking.beans.AccountBalanceResp;
import com.twinero.jtasks.nm.simplebanking.beans.Session;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
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

@RunWith(SpringRunner.class)
@WebMvcTest(SimpleBankingController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class WebLayerAccountBalancesTest
{
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SimpleBankService service;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------ shouldReturnAnAccountBalance
	/**
	 * Gets the account balance.
	 * @throws Exception Data Error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldReturnAnAccountBalance ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		AccountBalance balance = new AccountBalance();
		AccountBalanceResp accountBalanceResp = new AccountBalanceResp(balance, AccountBalanceResp.Status.OK);

		when(service.getAccountBalance(session.getClientID(), session.getSessionID())).thenReturn(accountBalanceResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountBalances/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(Util.asJsonString(accountBalanceResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountBalances/accountBalance"))
				.andReturn();
		
		verify(service, only()).getAccountBalance(session.getClientID(), session.getSessionID());
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------ shouldNotReturnTheAccountBalanceBecauseSessionDoesNotExist
	/**
	 * Tries to get the account balance, but the session is expired.
	 * @throws Exception Data Error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotReturnTheAccountBalanceBecauseExpiredSession ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		AccountBalance balance = new AccountBalance();
		AccountBalanceResp accountBalanceResp = new AccountBalanceResp(balance,
				AccountBalanceResp.Status.SESSION_EXPIRED);

		when(service.getAccountBalance(session.getClientID(), session.getSessionID())).thenReturn(accountBalanceResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountBalances/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(accountBalanceResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountBalances/expiredSession"))
				.andReturn();
		
		verify(service, only()).getAccountBalance(session.getClientID(), session.getSessionID());
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------ shouldNotReturnTheAccountBalanceBecauseSessionDoesNotExist
	/**
	 * Tries to get the account balance, but the session doesn't exist.
	 * @throws Exception Data Error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotReturnTheAccountBalanceBecauseSessionDoesNotExist ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		AccountBalance balance = new AccountBalance();
		AccountBalanceResp accountBalanceResp = new AccountBalanceResp(balance,
				AccountBalanceResp.Status.SESSION_DOES_NOT_EXIST);

		when(service.getAccountBalance(session.getClientID(), session.getSessionID())).thenReturn(accountBalanceResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountBalances/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(accountBalanceResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountBalances/sessionDoesNotExist"))
				.andReturn();
		
		verify(service, only()).getAccountBalance(session.getClientID(), session.getSessionID());
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------- shouldNotReturnTheAccountBalanceBecauseServerError
	/**
	 * Tries to get the account balance, but a server error occurs.
	 * @throws Exception Data Error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotReturnTheAccountBalanceBecauseServerError ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		AccountBalance balance = new AccountBalance();
		AccountBalanceResp accountBalanceResp = new AccountBalanceResp(balance,
				AccountBalanceResp.Status.SERVER_ERROR);

		when(service.getAccountBalance(session.getClientID(), session.getSessionID()))
				.thenThrow(SimpleBankServiceException.class);

		this.mockMvc
				.perform(get("/simpleBanking/accountBalances/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content().string(containsString(Util.asJsonString(accountBalanceResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountBalances/notAccountBalanceServerError"))
				.andReturn();
		
		verify(service, only()).getAccountBalance(session.getClientID(), session.getSessionID());
	}
}
