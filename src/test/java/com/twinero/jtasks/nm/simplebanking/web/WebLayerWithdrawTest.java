package com.twinero.jtasks.nm.simplebanking.web;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Withdraw;
import com.twinero.jtasks.nm.simplebanking.repository.beans.WithdrawResp;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.utils.Util;
import com.twinero.jtasks.nm.simplebanking.web.SimpleBankingController;
import com.twinero.jtasks.nm.simplebanking.web.beans.WithdrawReq;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SimpleBankingController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class WebLayerWithdrawTest
{
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SimpleBankService service;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------ shouldDoWithdraw
	/**
	 * Performs a success withdraw.
	 * @throws Exception Data Error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldDoWithdraw ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		Withdraw withdrawForReq = new Withdraw();

		withdrawForReq.setClientID(session.getClientID());
		WithdrawReq withdrawReq = new WithdrawReq(withdrawForReq, session.getSessionID());

		Withdraw withdrawForResp = new Withdraw(98785);
		WithdrawResp withdrawResp = new WithdrawResp(withdrawForResp, WithdrawResp.Status.OK);

		when(service.doWithdraw(withdrawForReq, session.getSessionID())).thenReturn((withdrawResp));

		this.mockMvc
				.perform(post("/simpleBanking/withdraws")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(withdrawReq))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString(Util.asJsonString(withdrawResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("withdraws/withdraw"))
				.andReturn();
	}

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------- shouldNotWithdrawBecauseInvalidClient
	/**
	 * Performs a not valid withdraw (invalid client).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotWithdrawBecauseInvalidClient ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		Withdraw withdrawForReq = new Withdraw();

		withdrawForReq.setClientID(session.getClientID());
		WithdrawReq withdrawReq = new WithdrawReq(withdrawForReq, session.getSessionID());

		Withdraw withdrawForResp = new Withdraw();
		WithdrawResp withdrawResp = new WithdrawResp(withdrawForResp, WithdrawResp.Status.INVALID_CLIENT);

		when(service.doWithdraw(withdrawForReq, session.getSessionID())).thenReturn((withdrawResp));

		this.mockMvc
				.perform(post("/simpleBanking/withdraws")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(withdrawReq))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isConflict())
				.andExpect(content().string(containsString(Util.asJsonString(withdrawResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("withdraws/invalidClient"))
				.andReturn();
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------- shouldNotWithdrawBecauseExpiredSession
	/**
	 * Performs a not valid withdraw (expired session).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotWithdrawBecauseExpiredSession ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		Withdraw withdrawForReq = new Withdraw();

		withdrawForReq.setClientID(session.getClientID());
		WithdrawReq withdrawReq = new WithdrawReq(withdrawForReq, session.getSessionID());

		Withdraw withdrawForResp = new Withdraw();
		WithdrawResp withdrawResp = new WithdrawResp(withdrawForResp, WithdrawResp.Status.SESSION_EXPIRED);

		when(service.doWithdraw(withdrawForReq, session.getSessionID())).thenReturn((withdrawResp));

		this.mockMvc
				.perform(post("/simpleBanking/withdraws")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(withdrawReq))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(withdrawResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("withdraws/expiredSession"))
				.andReturn();
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------- shouldNotWithdrawBecauseInvalidSession
	/**
	 * Performs a not valid withdraw (invalid session - does not exist).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotWithdrawBecauseInvalidSession ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		Withdraw withdrawForReq = new Withdraw();

		withdrawForReq.setClientID(session.getClientID());
		WithdrawReq withdrawReq = new WithdrawReq(withdrawForReq, session.getSessionID());

		Withdraw withdrawForResp = new Withdraw();
		WithdrawResp withdrawResp = new WithdrawResp(withdrawForResp, WithdrawResp.Status.SESSION_DOES_NOT_EXIST);

		when(service.doWithdraw(withdrawForReq, session.getSessionID())).thenReturn((withdrawResp));

		this.mockMvc
				.perform(post("/simpleBanking/withdraws")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(withdrawReq))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(withdrawResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("withdraws/sessionDoesNotExist"))
				.andReturn();
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------- shouldNotWithdrawBecauseServerError
	/**
	 * Performs a not valid withdraw (server error).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotWithdrawBecauseServerError ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		Withdraw withdrawForReq = new Withdraw();

		withdrawForReq.setClientID(session.getClientID());
		WithdrawReq withdrawReq = new WithdrawReq(withdrawForReq, session.getSessionID());

		Withdraw withdrawForResp = new Withdraw();
		WithdrawResp withdrawResp = new WithdrawResp(withdrawForResp, WithdrawResp.Status.SERVER_ERROR);

		when(service.doWithdraw(withdrawForReq, session.getSessionID())).thenThrow(SimpleBankServiceException.class);

		this.mockMvc
				.perform(post("/simpleBanking/withdraws")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(withdrawReq))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content().string(containsString(Util.asJsonString(withdrawResp))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("withdraws/serverError"))
				.andReturn();
	}
}
