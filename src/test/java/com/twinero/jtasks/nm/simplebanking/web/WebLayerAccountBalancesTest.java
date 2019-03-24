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
import com.twinero.jtasks.nm.simplebanking.service.beans.Balance;
import com.twinero.jtasks.nm.simplebanking.service.beans.BalanceResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.Session;
import com.twinero.jtasks.nm.simplebanking.utils.Util;
import com.twinero.jtasks.nm.simplebanking.web.SimpleBankingController;
import com.twinero.jtasks.nm.simplebanking.web.beans.BalanceDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.BalanceRespDTO;

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
import java.util.UUID;

@RunWith(SpringRunner.class)
@WebMvcTest(SimpleBankingController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class WebLayerAccountBalancesTest
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
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID sessionUUID = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		Date date = new Date();

		Balance expectedAccountBalance = new Balance();
		expectedAccountBalance.setClientID(clientID);
		expectedAccountBalance.setDate(date);
		expectedAccountBalance.setAvailable(new BigDecimal(2764.53));
		expectedAccountBalance.setBlocked(new BigDecimal(367.45));
		expectedAccountBalance.setDeferred(new BigDecimal(182.81));
		expectedAccountBalance.setTotal(new BigDecimal(7650.02));

		BalanceResp expectedAccountBalanceResp = new BalanceResp(expectedAccountBalance,
				BalanceResp.SessionStatus.OK);

		BalanceDTO expectedAccountBalanceDTO = new BalanceDTO();
		expectedAccountBalanceDTO.setClientID(clientID);

		expectedAccountBalanceDTO.setDate(date, DATE_FORMAT, TIME_ZONE);
		expectedAccountBalanceDTO.setAvailable(new BigDecimal(2764.53));
		expectedAccountBalanceDTO.setBlocked(new BigDecimal(367.45));
		expectedAccountBalanceDTO.setDeferred(new BigDecimal(182.81));
		expectedAccountBalanceDTO.setTotal(new BigDecimal(7650.02));

		BalanceRespDTO expectedAccountBalanceRespDTO = new BalanceRespDTO(expectedAccountBalanceDTO,
				BalanceRespDTO.SessionStatus.OK);

		when(service.getBalance(session.getClientID(), sessionUUID))
				.thenReturn(expectedAccountBalanceResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountBalances/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content()
						.string(containsString(
								Util.asJsonString(expectedAccountBalanceRespDTO, DATE_STRING))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("accountBalances/accountBalance"))
				.andReturn();

		verify(service, only()).getBalance(session.getClientID(), sessionUUID);
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
		long clientID = 10746;
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID sessionUUID = UUID.fromString(sessionID);

		Session session = new Session(sessionID);
		session.setClientID(clientID);

		BalanceResp expectedAccountBalanceResp = new BalanceResp(null,
				BalanceResp.SessionStatus.EXPIRED);

		BalanceRespDTO expectedAccountBalanceRespDTO = new BalanceRespDTO(null,
				BalanceRespDTO.SessionStatus.EXPIRED);

		when(service.getBalance(session.getClientID(), sessionUUID))
				.thenReturn(expectedAccountBalanceResp);

		this.mockMvc
				.perform(get("/simpleBanking/accountBalances/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content()
						.string(containsString(
								Util.asJsonString(expectedAccountBalanceRespDTO, DATE_STRING))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("accountBalances/expiredSession"))
				.andReturn();

		verify(service, only()).getBalance(session.getClientID(), sessionUUID);
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
		long clientID = 10746;
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID sessionUUID = UUID.fromString(sessionID);
		
		Session session = new Session(sessionID);
		session.setClientID(clientID);
		
		BalanceResp expectedAccountBalanceResp = new BalanceResp(null,
				BalanceResp.SessionStatus.DOES_NOT_EXIST);
		
		BalanceRespDTO expectedAccountBalanceRespDTO = new BalanceRespDTO(null,
				BalanceRespDTO.SessionStatus.DOES_NOT_EXIST);
		
		when(service.getBalance(session.getClientID(), sessionUUID))
				.thenReturn(expectedAccountBalanceResp);
		
		this.mockMvc
				.perform(get("/simpleBanking/accountBalances/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content()
						.string(containsString(
								Util.asJsonString(expectedAccountBalanceRespDTO, DATE_STRING))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("accountBalances/sessionDoesNotExist"))
				.andReturn();
		
		verify(service, only()).getBalance(session.getClientID(), sessionUUID);
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
		long clientID = 10746;
		String sessionID = "5dd35b40-2410-11e9-b56e-0800200c9a66";
		UUID sessionUUID = UUID.fromString(sessionID);
		
		Session session = new Session(sessionID);
		session.setClientID(clientID);
		
		BalanceRespDTO expectedAccountBalanceRespDTO = new BalanceRespDTO(null,
				BalanceRespDTO.SessionStatus.UNDEFINED);
		
		when(service.getBalance(session.getClientID(), sessionUUID))
				.thenThrow(SimpleBankServiceException.class);
		
		this.mockMvc
				.perform(get("/simpleBanking/accountBalances/" + session.getClientID())
						.header("Authorization", session.getSessionID())
						.characterEncoding(CHARACTER_ENCODING))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content()
						.string(containsString(
								Util.asJsonString(expectedAccountBalanceRespDTO, DATE_STRING))))
				.andExpect(content().contentType(CONTENT_TYPE))
				.andDo(document("accountBalances/notAccountBalanceServerError"))
				.andReturn();
		
		verify(service, only()).getBalance(session.getClientID(), sessionUUID);
	}
}
