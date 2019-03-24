package com.twinero.jtasks.nm.simplebanking.web;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;

import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.Statement;
import com.twinero.jtasks.nm.simplebanking.service.beans.StatementResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.Movement;
import com.twinero.jtasks.nm.simplebanking.service.beans.Session;
import com.twinero.jtasks.nm.simplebanking.utils.Util;
import com.twinero.jtasks.nm.simplebanking.web.SimpleBankingController;
import com.twinero.jtasks.nm.simplebanking.web.beans.StatementDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.StatementRespDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.MovementDTO;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RunWith(SpringRunner.class)
@WebMvcTest(SimpleBankingController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class WebLayerAccountStatementsTest
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID sessionUUID = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		Set<Movement> expectedMovements = new HashSet<Movement>();

		Movement expectedMovement_01 = new Movement(18534);
		expectedMovement_01.setTime(new Date(1547645112000L)); // 2019-01-16T09:25:12.000-04:00
		expectedMovement_01.setAmount(new BigDecimal(2109.43));
		expectedMovement_01.setDescription("Branch deposit");
		expectedMovement_01.setReference("18702753");
		expectedMovement_01.setTax(new BigDecimal(0.01));
		expectedMovement_01.setType(Movement.Type.DEPOSIT);

		Movement expectedMovement_02 = new Movement(908728);
		expectedMovement_02.setTime(new Date(1547857051000L)); // 2019-01-18T20:17:31.000-04:00
		expectedMovement_02.setAmount(new BigDecimal(187.50));
		expectedMovement_02.setDescription("Toy buy");
		expectedMovement_02.setReference("729601");
		expectedMovement_02.setTax(new BigDecimal(0.02));
		expectedMovement_02.setType(Movement.Type.WITHDRAW);

		expectedMovements.add(expectedMovement_01);
		expectedMovements.add(expectedMovement_02);

		Statement expectedAccountStatement = new Statement(998);
		expectedAccountStatement.setAccountNumber("12345678901234567890");
		expectedAccountStatement.setClientID(clientID);
		expectedAccountStatement.setSince(new Date(1546315200000L)); // 2019-01-01T00:00:00.000-04:00
		expectedAccountStatement.setUntil(new Date(1546401599999L)); // 2019-01-31T23:59:59.999-04:00

		expectedAccountStatement.setMovements(expectedMovements);

		StatementResp expectedAccountStatementResp = new StatementResp(expectedAccountStatement,
				StatementResp.SessionStatus.OK);

		StatementDTO expectedAccountStatementDTO = new StatementDTO(998);
		expectedAccountStatementDTO.setAccountNumber("12345678901234567890");
		expectedAccountStatementDTO.setClientID(clientID);
		expectedAccountStatementDTO.setSince(new Date(1546315200000L), DATE_FORMAT, TIME_ZONE); // 2019-01-01T00:00:00.000-04:00
		expectedAccountStatementDTO.setUntil(new Date(1546401599999L), DATE_FORMAT, TIME_ZONE); // 2019-01-31T23:59:59.999-04:00

		Set<MovementDTO> expectedMovementsDTO = new HashSet<MovementDTO>();

		MovementDTO expectedMovement_DTO_01 = new MovementDTO(18534);
		expectedMovement_DTO_01.setDate(new Date(1547645112000L), DATE_FORMAT, TIME_ZONE); // 2019-01-16T09:25:12.741-04:00
		expectedMovement_DTO_01.setAmount(new BigDecimal(2109.43));
		expectedMovement_DTO_01.setDescription("Branch deposit");
		expectedMovement_DTO_01.setReference("18702753");
		expectedMovement_DTO_01.setTax(new BigDecimal(0.01));
		expectedMovement_DTO_01.setType(MovementDTO.Type.DEPOSIT);

		MovementDTO expectedMovement_DTO_02 = new MovementDTO(908728);
		expectedMovement_DTO_02.setDate(new Date(1547857051000L), DATE_FORMAT, TIME_ZONE); // 2019-01-18T20:17:31.298-04:00
		expectedMovement_DTO_02.setAmount(new BigDecimal(187.50));
		expectedMovement_DTO_02.setDescription("Toy buy");
		expectedMovement_DTO_02.setReference("729601");
		expectedMovement_DTO_02.setTax(new BigDecimal(0.02));
		expectedMovement_DTO_02.setType(MovementDTO.Type.WITHDRAW);

		expectedMovementsDTO.add(expectedMovement_DTO_01);
		expectedMovementsDTO.add(expectedMovement_DTO_02);

		expectedAccountStatementDTO.setMovementsDTO(expectedMovementsDTO);

		StatementRespDTO expectedAccountStatementRespDTO = new StatementRespDTO(expectedAccountStatementDTO,
				StatementRespDTO.SessionStatus.OK);

		when(service.getStatement(session.getClientID(), sessionUUID))
				.thenReturn(expectedAccountStatementResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountStatements/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isOk())
				//.andExpect(content().string(containsString(Util.asJsonString(expectedAccountStatementRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("accountStatements/accountStatement"))
				.andReturn();

		verify(service, only()).getStatement(session.getClientID(), sessionUUID);
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID sessionUUID = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		StatementResp statementResp = new StatementResp(null,
				StatementResp.SessionStatus.EXPIRED);

		StatementRespDTO statementRespDTO = new StatementRespDTO(null,
				StatementRespDTO.SessionStatus.EXPIRED);

		when(service.getStatement(session.getClientID(), sessionUUID))
				.thenReturn(statementResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountStatements/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(statementRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("accountStatements/expiredSession"))
				.andReturn();

		verify(service, only()).getStatement(session.getClientID(), sessionUUID);
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID sessionUUID = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		StatementResp statementResp = new StatementResp(null,
				StatementResp.SessionStatus.DOES_NOT_EXIST);

		StatementRespDTO statementRespDTO = new StatementRespDTO(null,
				StatementRespDTO.SessionStatus.DOES_NOT_EXIST);

		when(service.getStatement(session.getClientID(), sessionUUID))
				.thenReturn(statementResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountStatements/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(statementRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("accountStatements/sessionDoesNotExist"))
				.andReturn();

		verify(service, only()).getStatement(session.getClientID(), sessionUUID);
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID sessionUUID = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		StatementRespDTO statementRespDTO = new StatementRespDTO(null,
				StatementRespDTO.SessionStatus.UNDEFINED);

		when(service.getStatement(session.getClientID(), sessionUUID))
				.thenThrow(SimpleBankServiceException.class);

		this.mockMvc
				.perform(get("/simpleBanking/accountStatements/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content().string(containsString(Util.asJsonString(statementRespDTO))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("accountStatements/notAccountStatementServerError"))
				.andReturn();

		verify(service, only()).getStatement(session.getClientID(), sessionUUID);
	}
}
