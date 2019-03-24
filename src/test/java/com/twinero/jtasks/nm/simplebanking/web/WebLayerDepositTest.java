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
import com.twinero.jtasks.nm.simplebanking.service.beans.MovementResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.Session;
import com.twinero.jtasks.nm.simplebanking.service.beans.Movement;
import com.twinero.jtasks.nm.simplebanking.utils.Util;
import com.twinero.jtasks.nm.simplebanking.web.SimpleBankingController;
import com.twinero.jtasks.nm.simplebanking.web.beans.DepositDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.DepositReqDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.DepositRespDTO;

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
public class WebLayerDepositTest
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID uuidSession = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		// time = 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Movement deposit = new Movement();
		deposit.setClientID(session.getClientID());
		deposit.setAmount(new BigDecimal(1250.25));
		deposit.setTime(time);

		DepositDTO depositDTO = new DepositDTO();
		depositDTO.setClientID(session.getClientID());
		depositDTO.setAmount(new BigDecimal(1250.25));
		depositDTO.setDateTime(time, DATE_FORMAT, TIME_ZONE);
		DepositReqDTO depositReqDTO = new DepositReqDTO(depositDTO, session.getSessionID());

		long expectedDepositID = 123;
		BigDecimal expectedMount = new BigDecimal(1250.25);
		String expectedReference = "165432876";

		Movement expectedDeposit = new Movement(expectedDepositID);
		expectedDeposit.setClientID(session.getClientID());
		expectedDeposit.setAmount(expectedMount);
		expectedDeposit.setTime(time);
		expectedDeposit.setReference(expectedReference);
		expectedDeposit.setType(Movement.Type.DEPOSIT);
		MovementResp expectedDepositResp = new MovementResp(expectedDeposit, MovementResp.SessionStatus.OK);

		DepositDTO expectedDepositDTO = new DepositDTO(expectedDepositID);
		expectedDepositDTO.setClientID(session.getClientID());
		expectedDepositDTO.setAmount(expectedMount);
		expectedDepositDTO.setDateTime(time, DATE_FORMAT, TIME_ZONE);
		expectedDepositDTO.setReference(expectedReference);
		DepositRespDTO expectedDepositRespDTO = new DepositRespDTO(expectedDepositDTO, DepositRespDTO.SessionStatus.OK);

		when(service.doDeposit(deposit, uuidSession)).thenReturn((expectedDepositResp));

		try
		{
			this.mockMvc
					.perform(post("/simpleBanking/deposits")
							.contentType(MediaType.APPLICATION_JSON)
							.content(Util.asJsonString(depositReqDTO))
							.characterEncoding(CHARACTER_ENCODING))
					.andDo(print())
					.andExpect(status().isCreated())
					.andExpect(content().string(containsString(Util.asJsonString(expectedDepositRespDTO))))
					.andExpect(content().contentType(CONTENT_TYPE))
					.andDo(document("deposits/deposit"))
					.andReturn();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		verify(service, only()).doDeposit(deposit, uuidSession);
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID uuidSession = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		// time = 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Movement deposit = new Movement();
		deposit.setClientID(session.getClientID());
		deposit.setAmount(new BigDecimal(1250.25));
		deposit.setTime(time);

		DepositDTO depositDTO = new DepositDTO();
		depositDTO.setClientID(session.getClientID());
		depositDTO.setAmount(new BigDecimal(1250.25));
		depositDTO.setDateTime(time, DATE_FORMAT, TIME_ZONE);
		DepositReqDTO depositReqDTO = new DepositReqDTO(depositDTO, session.getSessionID());

		Movement expectedDeposit = new Movement();
		MovementResp expectedDepositResp = new MovementResp(expectedDeposit, MovementResp.SessionStatus.OK);

		DepositDTO expectedDepositDTO = new DepositDTO();
		DepositRespDTO expectedDepositRespDTO = new DepositRespDTO(expectedDepositDTO, DepositRespDTO.SessionStatus.OK);

		when(service.doDeposit(deposit, uuidSession)).thenReturn((expectedDepositResp));

		this.mockMvc
				.perform(post("/simpleBanking/deposits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(depositReqDTO))
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isConflict())
				.andExpect(content().string(containsString(Util.asJsonString(expectedDepositRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("deposits/invalidClient"))
				.andReturn();

		verify(service, only()).doDeposit(deposit, uuidSession);
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID uuidSession = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		// time = 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Movement deposit = new Movement();
		deposit.setClientID(session.getClientID());
		deposit.setAmount(new BigDecimal(1250.25));
		deposit.setTime(time);

		DepositDTO depositDTO = new DepositDTO();
		depositDTO.setClientID(session.getClientID());
		depositDTO.setAmount(new BigDecimal(1250.25));
		depositDTO.setDateTime(time, DATE_FORMAT, TIME_ZONE);
		DepositReqDTO depositReqDTO = new DepositReqDTO(depositDTO, session.getSessionID());

		MovementResp expectedDepositResp = new MovementResp(null, MovementResp.SessionStatus.EXPIRED);
		DepositRespDTO expectedDepositRespDTO = new DepositRespDTO(null, DepositRespDTO.SessionStatus.EXPIRED);

		when(service.doDeposit(deposit, uuidSession)).thenReturn((expectedDepositResp));

		this.mockMvc
				.perform(post("/simpleBanking/deposits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(depositReqDTO))
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(expectedDepositRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("deposits/expiredSession"))
				.andReturn();

		verify(service, only()).doDeposit(deposit, uuidSession);
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID uuidSession = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		// time = 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Movement deposit = new Movement();
		deposit.setClientID(session.getClientID());
		deposit.setAmount(new BigDecimal(1250.25));
		deposit.setTime(time);

		DepositDTO depositDTO = new DepositDTO();
		depositDTO.setClientID(session.getClientID());
		depositDTO.setAmount(new BigDecimal(1250.25));
		depositDTO.setDateTime(time, DATE_FORMAT, TIME_ZONE);
		DepositReqDTO depositReqDTO = new DepositReqDTO(depositDTO, session.getSessionID());

		MovementResp expectedDepositResp = new MovementResp(null, MovementResp.SessionStatus.DOES_NOT_EXIST);
		DepositRespDTO expectedDepositRespDTO = new DepositRespDTO(null, DepositRespDTO.SessionStatus.DOES_NOT_EXIST);

		when(service.doDeposit(deposit, uuidSession)).thenReturn((expectedDepositResp));

		this.mockMvc
				.perform(post("/simpleBanking/deposits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(depositReqDTO))
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(expectedDepositRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("deposits/sessionDoesNotExist"))
				.andReturn();

		verify(service, only()).doDeposit(deposit, uuidSession);
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID uuidSession = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		// time = 2019-02-24T14:00:27.87-04:00
		Date time = new Date(1551031227087L);

		Movement deposit = new Movement();
		deposit.setClientID(session.getClientID());
		deposit.setAmount(new BigDecimal(1250.25));
		deposit.setTime(time);

		DepositDTO depositDTO = new DepositDTO();
		depositDTO.setClientID(session.getClientID());
		depositDTO.setAmount(new BigDecimal(1250.25));
		depositDTO.setDateTime(time, DATE_FORMAT, TIME_ZONE);
		DepositReqDTO depositReqDTO = new DepositReqDTO(depositDTO, session.getSessionID());

		DepositRespDTO expectedDepositRespDTO = new DepositRespDTO(null, DepositRespDTO.SessionStatus.UNDEFINED);

		when(service.doDeposit(deposit, uuidSession)).thenThrow(SimpleBankServiceException.class);

		this.mockMvc
				.perform(post("/simpleBanking/deposits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(depositReqDTO))
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content().string(containsString(Util.asJsonString(expectedDepositRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("deposits/serverError"))
				.andReturn();

		verify(service, only()).doDeposit(deposit, uuidSession);
	}
}
