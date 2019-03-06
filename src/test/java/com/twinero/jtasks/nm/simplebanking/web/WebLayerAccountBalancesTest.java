package com.twinero.jtasks.nm.simplebanking.web;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;

import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountBalance;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.AccountBalanceResp;
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
import java.util.Date;

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
		long clientID = 10746;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		AccountBalance expectedAccountBalance = new AccountBalance(54321);
		expectedAccountBalance.setClientID(clientID);
		expectedAccountBalance.setDate(new Date());
		expectedAccountBalance.setAvailable(new BigDecimal(2764.53));
		expectedAccountBalance.setBlocked(new BigDecimal(367.45));
		expectedAccountBalance.setDeferred(new BigDecimal(182.81));
		expectedAccountBalance.setTotal(new BigDecimal(7650.02));

		AccountBalanceResp expectedAccountBalanceResp = new AccountBalanceResp(expectedAccountBalance,
				AccountBalanceResp.Status.OK);

		when(service.getAccountBalance(session.getClientID(), session.getSessionID()))
				.thenReturn(expectedAccountBalanceResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountBalances/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content()
						.string(containsString(Util.asJsonString(expectedAccountBalanceResp, AccountBalance.DATE_FORMAT))))
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

		AccountBalance expectedAccountBalance = new AccountBalance();
		AccountBalanceResp expectedAccountBalanceResp = new AccountBalanceResp(expectedAccountBalance,
				AccountBalanceResp.Status.SESSION_EXPIRED);

		when(service.getAccountBalance(session.getClientID(), session.getSessionID()))
				.thenReturn(expectedAccountBalanceResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountBalances/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content()
						.string(containsString(Util.asJsonString(expectedAccountBalanceResp, AccountBalance.DATE_FORMAT))))
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

		AccountBalance expectedAccountBalance = new AccountBalance();
		AccountBalanceResp expectedAccountBalanceResp = new AccountBalanceResp(expectedAccountBalance,
				AccountBalanceResp.Status.SESSION_DOES_NOT_EXIST);

		when(service.getAccountBalance(session.getClientID(), session.getSessionID()))
				.thenReturn(expectedAccountBalanceResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountBalances/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content()
						.string(containsString(Util.asJsonString(expectedAccountBalanceResp, AccountBalance.DATE_FORMAT))))
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

		AccountBalance expectedAccountBalance = new AccountBalance();
		AccountBalanceResp expectedAccountBalanceResp = new AccountBalanceResp(expectedAccountBalance,
				AccountBalanceResp.Status.SERVER_ERROR);

		when(service.getAccountBalance(session.getClientID(), session.getSessionID()))
				.thenThrow(SimpleBankServiceException.class);

		this.mockMvc
				.perform(get("/simpleBanking/accountBalances/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content()
						.string(containsString(Util.asJsonString(expectedAccountBalanceResp, AccountBalance.DATE_FORMAT))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("accountBalances/notAccountBalanceServerError"))
				.andReturn();

		verify(service, only()).getAccountBalance(session.getClientID(), session.getSessionID());
	}
}
