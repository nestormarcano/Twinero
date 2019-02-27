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

import com.twinero.jtasks.nm.simplebanking.beans.Session;
import com.twinero.jtasks.nm.simplebanking.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.utils.Util;
import com.twinero.jtasks.nm.simplebanking.web.SimpleBankingController;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SimpleBankingController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class WebLayerSessionsTest
{
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SimpleBankService service;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------ shouldLoginAndReturnASession
	/**
	 * Performs a login and return a valid session.
	 * 
	 * @throws Exception Data Error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldLoginAndReturnASession ()
		throws Exception
	{
		String email = "nestor.marcano@gmail.com";
		String password = "123456";
		Sign sign = new Sign(email, password);

		long clientID = 10;
		Session expectedSession = new Session("5dd35b40-2410-11e9-b56e-0800200c9a66");
		expectedSession.setClientID(clientID);
		expectedSession.setSessionStatus(Session.Status.OK);
		
		when(service.login(sign)).thenReturn(expectedSession);

		this.mockMvc
				.perform(post("/simpleBanking/sessions")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(sign))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString(Util.asJsonString(expectedSession))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("sessions/login"))
				.andReturn();
		
		verify(service, only()).login(sign);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------------------- shouldNotLogin
	/**
	 * Performs a not valid login.
	 * 
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotLogin ()
		throws Exception
	{
		String email = "pedro.marcano@gmail.com";
		String password = "123456";
		Sign sign = new Sign(email, password);
		
		Session expectedSession = new Session();
		expectedSession.setSessionStatus(Session.Status.UNAUTHORIZED);
		
		when(service.login(sign)).thenReturn(expectedSession);

		this.mockMvc
				.perform(post("/simpleBanking/sessions")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(sign))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(containsString(Util.asJsonString(expectedSession))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("sessions/notLogin"))
				.andReturn();
		
		verify(service, only()).login(sign);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------- shouldNotLoginBecauseMalformedEmail
	/**
	 * Performs a not valid login because malformed email.
	 * 
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotLoginBecauseMalformedEmail ()
		throws Exception
	{
		String email = "nestor marcano gmail.com";
		String password = "123 456";
		
		Sign sign = new Sign(email, password);
		Session expectedSession = new Session();

		this.mockMvc
				.perform(post("/simpleBanking/sessions")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(sign))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString(Util.asJsonString(expectedSession))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("sessions/notLoginMalformedEmail"))
				.andReturn();
		
		verify(service, times(0)).login(sign);
	}
	
	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------- shouldNotLoginServerError
	/**
	 * Performs a not valid login because a server error.
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotLoginServerError ()
		throws Exception
	{
		String email = "nestor.marcano@gmail.com";
		String password = "123456";
		
		Sign sign = new Sign(email, password);
		Session expectedSession = new Session();
		
		when(service.login(sign)).thenThrow(SimpleBankServiceException.class);

		this.mockMvc
				.perform(post("/simpleBanking/sessions")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(sign))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content().string(containsString(Util.asJsonString(expectedSession))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("sessions/notLoginServerError"))
				.andReturn();
		
		verify(service, only()).login(sign);
	}
}
