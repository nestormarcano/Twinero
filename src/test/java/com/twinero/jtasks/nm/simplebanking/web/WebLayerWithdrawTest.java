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

import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.Session;
import com.twinero.jtasks.nm.simplebanking.service.beans.Movement;
import com.twinero.jtasks.nm.simplebanking.service.beans.MovementResp;
import com.twinero.jtasks.nm.simplebanking.utils.Util;
import com.twinero.jtasks.nm.simplebanking.web.SimpleBankingController;
import com.twinero.jtasks.nm.simplebanking.web.beans.WithdrawDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.WithdrawReqDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.WithdrawRespDTO;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringRunner.class)
@WebMvcTest(SimpleBankingController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class WebLayerWithdrawTest
{
	private static final String TIME_ZONE = "GMT-4";
	private static final String DATE_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_STRING);
	private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
	private static final String CHARACTER_ENCODING = "UTF-8";

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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID uuidSession = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		// time = 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Movement withdraw = new Movement();
		withdraw.setClientID(session.getClientID());
		withdraw.setAmount(new BigDecimal(1250.25));
		withdraw.setTime(time);

		WithdrawDTO withdrawDTO = new WithdrawDTO();
		withdrawDTO.setClientID(session.getClientID());
		withdrawDTO.setAmount(new BigDecimal(1250.25));
		withdrawDTO.setDateTime(time, DATE_FORMAT, TIME_ZONE);
		WithdrawReqDTO withdrawReqDTO = new WithdrawReqDTO(withdrawDTO, session.getSessionID());

		long expectedWithdrawID = 123;
		BigDecimal expectedMount = new BigDecimal(1250.25);
		String expectedReference = "165432876";

		Movement expectedWithdraw = new Movement(expectedWithdrawID);
		expectedWithdraw.setClientID(session.getClientID());
		expectedWithdraw.setAmount(expectedMount);
		expectedWithdraw.setTime(time);
		expectedWithdraw.setReference(expectedReference);
		expectedWithdraw.setType(Movement.Type.WITHDRAW);
		MovementResp expectedWithdrawResp = new MovementResp(expectedWithdraw, MovementResp.SessionStatus.OK);

		WithdrawDTO expectedWithdrawDTO = new WithdrawDTO(expectedWithdrawID);
		expectedWithdrawDTO.setClientID(session.getClientID());
		expectedWithdrawDTO.setAmount(expectedMount);
		expectedWithdrawDTO.setDateTime(time, DATE_FORMAT, TIME_ZONE);
		expectedWithdrawDTO.setReference(expectedReference);
		WithdrawRespDTO expectedWithdrawRespDTO = new WithdrawRespDTO(expectedWithdrawDTO,
				WithdrawRespDTO.SessionStatus.OK);

		when(service.doWithdraw(withdraw, uuidSession)).thenReturn((expectedWithdrawResp));

		this.mockMvc
				.perform(post("/simpleBanking/withdraws")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(withdrawReqDTO))
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString(Util.asJsonString(expectedWithdrawRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("withdraws/withdraw"))
				.andReturn();

		verify(service, only()).doWithdraw(withdraw, uuidSession);
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID uuidSession = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		// 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Movement withdraw = new Movement();
		withdraw.setClientID(session.getClientID());
		withdraw.setAmount(new BigDecimal(1250.25));
		withdraw.setTime(time);

		WithdrawDTO withdrawDTO = new WithdrawDTO();
		withdrawDTO.setClientID(session.getClientID());
		withdrawDTO.setAmount(new BigDecimal(1250.25));
		withdrawDTO.setDateTime(time, DATE_FORMAT, TIME_ZONE);

		WithdrawReqDTO withdrawReqDTO = new WithdrawReqDTO(withdrawDTO, session.getSessionID());

		Movement expectedWithdraw = new Movement();
		MovementResp expectedWithdrawResp = new MovementResp(expectedWithdraw, MovementResp.SessionStatus.OK);

		WithdrawDTO expectedWithdrawDTO = new WithdrawDTO();
		WithdrawRespDTO expectedWithdrawRespDTO = new WithdrawRespDTO(expectedWithdrawDTO,
				WithdrawRespDTO.SessionStatus.OK);

		when(service.doWithdraw(withdraw, uuidSession)).thenReturn((expectedWithdrawResp));

		this.mockMvc
				.perform(post("/simpleBanking/withdraws")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(withdrawReqDTO))
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isConflict())
				.andExpect(content().string(containsString(Util.asJsonString(expectedWithdrawRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("withdraws/invalidClient"))
				.andReturn();

		verify(service, only()).doWithdraw(withdraw, uuidSession);
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID uuidSession = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		// 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Movement withdraw = new Movement();
		withdraw.setClientID(session.getClientID());
		withdraw.setAmount(new BigDecimal(1250.25));
		withdraw.setTime(time);

		WithdrawDTO withdrawDTO = new WithdrawDTO();
		withdrawDTO.setClientID(session.getClientID());
		withdrawDTO.setAmount(new BigDecimal(1250.25));
		withdrawDTO.setDateTime(time, DATE_FORMAT, TIME_ZONE);

		WithdrawReqDTO withdrawReqDTO = new WithdrawReqDTO(withdrawDTO, session.getSessionID());

		MovementResp expectedWithdrawResp = new MovementResp(null, MovementResp.SessionStatus.EXPIRED);
		WithdrawRespDTO expectedWithdrawRespDTO = new WithdrawRespDTO(null, WithdrawRespDTO.SessionStatus.EXPIRED);

		when(service.doWithdraw(withdraw, uuidSession)).thenReturn((expectedWithdrawResp));

		this.mockMvc
				.perform(post("/simpleBanking/withdraws")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(withdrawReqDTO))
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(expectedWithdrawRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("withdraws/expiredSession"))
				.andReturn();

		verify(service, only()).doWithdraw(withdraw, uuidSession);
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID uuidSession = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		// 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Movement withdraw = new Movement();
		withdraw.setClientID(session.getClientID());
		withdraw.setAmount(new BigDecimal(1250.25));
		withdraw.setTime(time);

		WithdrawDTO withdrawDTO = new WithdrawDTO();
		withdrawDTO.setClientID(session.getClientID());
		withdrawDTO.setAmount(new BigDecimal(1250.25));
		withdrawDTO.setDateTime(time, DATE_FORMAT, TIME_ZONE);

		WithdrawReqDTO withdrawReqDTO = new WithdrawReqDTO(withdrawDTO, session.getSessionID());

		MovementResp expectedWithdrawResp = new MovementResp(null, MovementResp.SessionStatus.DOES_NOT_EXIST);
		WithdrawRespDTO expectedWithdrawRespDTO = new WithdrawRespDTO(null, WithdrawRespDTO.SessionStatus.DOES_NOT_EXIST);

		when(service.doWithdraw(withdraw, uuidSession)).thenReturn((expectedWithdrawResp));

		this.mockMvc
				.perform(post("/simpleBanking/withdraws")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(withdrawReqDTO))
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(expectedWithdrawRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("withdraws/sessionDoesNotExist"))
				.andReturn();

		verify(service, only()).doWithdraw(withdraw, uuidSession);
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID uuidSession = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);
		
		// 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);
		
		Movement withdraw = new Movement();
		withdraw.setClientID(session.getClientID());
		withdraw.setAmount(new BigDecimal(1250.25));
		withdraw.setTime(time);
		
		WithdrawDTO withdrawDTO = new WithdrawDTO();
		withdrawDTO.setClientID(session.getClientID());
		withdrawDTO.setAmount(new BigDecimal(1250.25));
		withdrawDTO.setDateTime(time, DATE_FORMAT, TIME_ZONE);
		
		WithdrawReqDTO withdrawReqDTO = new WithdrawReqDTO(withdrawDTO, session.getSessionID());
		WithdrawRespDTO expectedWithdrawRespDTO = new WithdrawRespDTO(null, WithdrawRespDTO.SessionStatus.UNDEFINED);
		
		when(service.doWithdraw(withdraw, uuidSession)).thenThrow(SimpleBankServiceException.class);
		
		this.mockMvc
				.perform(post("/simpleBanking/withdraws")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(withdrawReqDTO))
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content().string(containsString(Util.asJsonString(expectedWithdrawRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("withdraws/serverError"))
				.andReturn();
		
		verify(service, only()).doWithdraw(withdraw, uuidSession);
	}
}
