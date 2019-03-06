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

import com.twinero.jtasks.nm.simplebanking.repository.beans.Deposit;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.DepositResp;
import com.twinero.jtasks.nm.simplebanking.utils.Util;
import com.twinero.jtasks.nm.simplebanking.web.SimpleBankingController;
import com.twinero.jtasks.nm.simplebanking.web.beans.DepositReq;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringRunner.class)
@WebMvcTest(SimpleBankingController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class WebLayerDepositTest
{
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SimpleBankService service;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------- shouldDoDeposit
	/**
	 * Performs a success depositReq.
	 * @throws Exception Data Error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldDoDeposit ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		// time = 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Deposit deposit = new Deposit();
		deposit.setClientID(session.getClientID());
		deposit.setMount(new BigDecimal(1250.25));
		deposit.setTime(time);
		DepositReq depositReq = new DepositReq(deposit, session.getSessionID());

		long expectedDepositID = 123;
		BigDecimal expectedMount = new BigDecimal(1250.25);
		String expectedReference = "165432876";

		Deposit expectedDeposit = new Deposit(expectedDepositID);
		expectedDeposit.setClientID(session.getClientID());
		expectedDeposit.setMount(expectedMount);
		expectedDeposit.setTime(time);
		expectedDeposit.setReference(expectedReference);
		DepositResp expectedDepositResp = new DepositResp(expectedDeposit, DepositResp.Status.OK);

		when(service.doDeposit(deposit, session.getSessionID())).thenReturn((expectedDepositResp));

		this.mockMvc
				.perform(post("/simpleBanking/deposits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(depositReq, Deposit.DATE_FORMAT))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(
						content().string(containsString(Util.asJsonString(expectedDepositResp, Deposit.DATE_FORMAT))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("deposits/deposit"))
				.andReturn();

		verify(service, only()).doDeposit(deposit, session.getSessionID());
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------- shouldNotDepositBecauseInvalidClient
	/**
	 * Performs a not valid depositReq (invalid client).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotDepositBecauseInvalidClient ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		// time = 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Deposit deposit = new Deposit();
		deposit.setClientID(session.getClientID());
		deposit.setMount(new BigDecimal(1250.25));
		deposit.setTime(time);
		DepositReq depositReq = new DepositReq(deposit, session.getSessionID());

		Deposit expectedDeposit = new Deposit();
		DepositResp expectedDepositResp = new DepositResp(expectedDeposit, DepositResp.Status.INVALID_CLIENT);

		when(service.doDeposit(deposit, session.getSessionID())).thenReturn((expectedDepositResp));

		this.mockMvc
				.perform(post("/simpleBanking/deposits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(depositReq, Deposit.DATE_FORMAT))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isConflict())
				.andExpect(
						content().string(containsString(Util.asJsonString(expectedDepositResp, Deposit.DATE_FORMAT))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("deposits/invalidClient"))
				.andReturn();

		verify(service, only()).doDeposit(deposit, session.getSessionID());
	}

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------- shouldNotDepositBecauseExpiredSession
	/**
	 * Performs a not valid depositReq (expired session).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotDepositBecauseExpiredSession ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		// time = 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Deposit deposit = new Deposit();
		deposit.setClientID(session.getClientID());
		deposit.setMount(new BigDecimal(1250.25));
		deposit.setTime(time);
		DepositReq depositReq = new DepositReq(deposit, session.getSessionID());

		Deposit expectedDeposit = new Deposit();
		DepositResp expectedDepositResp = new DepositResp(expectedDeposit, DepositResp.Status.SESSION_EXPIRED);

		when(service.doDeposit(deposit, session.getSessionID())).thenReturn((expectedDepositResp));

		this.mockMvc
				.perform(post("/simpleBanking/deposits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(depositReq, Deposit.DATE_FORMAT))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(
						content().string(containsString(Util.asJsonString(expectedDepositResp, Deposit.DATE_FORMAT))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("deposits/expiredSession"))
				.andReturn();

		verify(service, only()).doDeposit(deposit, session.getSessionID());
	}

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------- shouldNotDepositBecauseInvalidSession
	/**
	 * Performs a not valid depositReq (invalid session - does not exist).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotDepositBecauseInvalidSession ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		// time = 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Deposit deposit = new Deposit();
		deposit.setClientID(session.getClientID());
		deposit.setMount(new BigDecimal(1250.25));
		deposit.setTime(time);
		DepositReq depositReq = new DepositReq(deposit, session.getSessionID());

		Deposit expectedDeposit = new Deposit();
		DepositResp expectedDepositResp = new DepositResp(expectedDeposit, DepositResp.Status.SESSION_DOES_NOT_EXIST);

		when(service.doDeposit(deposit, session.getSessionID())).thenReturn((expectedDepositResp));

		this.mockMvc
				.perform(post("/simpleBanking/deposits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(depositReq, Deposit.DATE_FORMAT))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(
						content().string(containsString(Util.asJsonString(expectedDepositResp, Deposit.DATE_FORMAT))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("deposits/sessionDoesNotExist"))
				.andReturn();

		verify(service, only()).doDeposit(deposit, session.getSessionID());
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------ shouldNotDepositBecauseServerError
	/**
	 * Performs a not valid depositReq (server error).
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotDepositBecauseServerError ()
		throws Exception
	{
		long clientID = 10;
		Session session = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		session.setClientID(clientID);

		// time = 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Deposit deposit = new Deposit();
		deposit.setClientID(session.getClientID());
		deposit.setMount(new BigDecimal(1250.25));
		deposit.setTime(time);
		DepositReq depositReq = new DepositReq(deposit, session.getSessionID());

		Deposit expectedDeposit = new Deposit();
		DepositResp expectedDepositResp = new DepositResp(expectedDeposit, DepositResp.Status.SERVER_ERROR);

		when(service.doDeposit(deposit, session.getSessionID())).thenThrow(SimpleBankServiceException.class);

		this.mockMvc
				.perform(post("/simpleBanking/deposits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(depositReq, Deposit.DATE_FORMAT))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(
						content().string(containsString(Util.asJsonString(expectedDepositResp, Deposit.DATE_FORMAT))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("deposits/serverError"))
				.andReturn();

		verify(service, only()).doDeposit(deposit, session.getSessionID());
	}
}
