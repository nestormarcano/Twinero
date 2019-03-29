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

import com.twinero.jtasks.nm.simplebanking.repository.beans.SignDAO;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.utils.Util;
import com.twinero.jtasks.nm.simplebanking.web.SimpleBankingController;
import com.twinero.jtasks.nm.simplebanking.web.beans.SignReqDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.SignRespDTO;

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

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@RunWith(SpringRunner.class)
@WebMvcTest(SimpleBankingController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class WebLayerSignupsTest
{
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SimpleBankService service;

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------- shouldSignupAndReturnOK
	/**
	 * Performs a valid SignDAO up.
	 * 
	 * @throws Exception Data Error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldSignupAndReturnOK ()
		throws Exception
	{
		long id = 123L;
		String email = "nestor.marcano@gmail.com";
		String password = "123456";

		SignDAO sign = new SignDAO(email, password);
		SignReqDTO signReqDTO = new SignReqDTO(email, password);

		SignDAO expectedSignFromService = new SignDAO(email, password);
		expectedSignFromService.setSignID(id);

		when(service.signup(sign)).thenReturn(expectedSignFromService);

		SignRespDTO expectedSignRespDTO = new SignRespDTO(SignRespDTO.Status.OK);
		expectedSignRespDTO.setSignID(id);
		expectedSignRespDTO.setEmail(email);
		
		this.mockMvc
				.perform(
						post("/simpleBanking/signups")
								.contentType(MediaType.APPLICATION_JSON)
								.content(Util.asJsonString(signReqDTO))
								.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString(Util.asJsonString(expectedSignRespDTO))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("signups/signup"))
				.andReturn();

		verify(service, only()).signup(sign);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------- shouldNotSignupAlreadyExist
	/**
	 * Performs a not valid sign up because the client already exists.
	 * @throws Exception Data Error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotSignupAlreadyExist ()
		throws Exception
	{
		String email = "pedro.marcano@gmail.com";
		String password = "123456";
		
		SignDAO sign = new SignDAO(email, password);
		SignReqDTO signReqDTO = new SignReqDTO(email, password);
		
		SignRespDTO expectedSignRespDTO = new SignRespDTO(SignRespDTO.Status.ALREADY_EXISTS);
		expectedSignRespDTO.setEmail(email);
		
		when(service.signup(sign)).thenThrow(ConstraintViolationException.class);
		
		this.mockMvc
				.perform(post("/simpleBanking/signups")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(signReqDTO))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isConflict())
				.andExpect(content().string(containsString(Util.asJsonString(expectedSignRespDTO))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("signups/notSignup"))
				.andReturn();
		
		verify(service, only()).signup(sign);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------- shouldNotSignupBecauseMalformedEmail
	/**
	 * Performs a not valid sign-up because malformed email.
	 * 
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotSignupBecauseMalformedEmail ()
		throws Exception
	{
		String email = "nestor marcano gmail.com";
		String password = "123456";

		SignDAO sign = new SignDAO(email, password);
		SignReqDTO signReqDTO = new SignReqDTO(email, password);

		String uriRequested = "/simpleBanking/signups";

		this.mockMvc
				.perform(post("/simpleBanking/signups")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(signReqDTO))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString(uriRequested)))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("signups/notSignupMalformedEmail"))
				.andReturn();

		verify(service, times(0)).signup(sign);
	}
	
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------- shouldNotSignupBecauseMalformedPassword
	/**
	 * Performs a not valid sign-up because malformed pasword.
	 * 
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotSignupBecauseMalformedPassword ()
		throws Exception
	{
		String email = "nestor.marcano@gmail.com";
		String password = "123 456";

		SignDAO sign = new SignDAO(email, password);
		SignReqDTO signReqDTO = new SignReqDTO(email, password);

		String message = "The password doesn't match the password criteria";
		String uriRequested = "/simpleBanking/signups";

		this.mockMvc
				.perform(post("/simpleBanking/signups")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(signReqDTO))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString(message)))
				.andExpect(content().string(containsString(uriRequested)))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("signups/notSignupMalformedPassword"))
				.andReturn();

		verify(service, times(0)).signup(sign);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------- shouldNotSignupServerError
	/**
	 * Performs a not valid sign-up because malformed email.
	 * 
	 * @throws Exception Data error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotSignupServerError ()
		throws Exception
	{
		String email = "nestor.marcano@gmail.com";
		String password = "123456";
		
		SignDAO sign = new SignDAO(email, password);
		SignReqDTO signReqDTO = new SignReqDTO(email, password);
		
		SignRespDTO expectedSignRespDTO = new SignRespDTO(SignRespDTO.Status.SERVER_ERROR);
		expectedSignRespDTO.setEmail(email);
		
		when(service.signup(sign)).thenThrow(ValidationException.class);
		
		this.mockMvc
				.perform(post("/simpleBanking/signups")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(signReqDTO))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content().string(containsString(Util.asJsonString(expectedSignRespDTO))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("signups/notSignupServerError"))
				.andReturn();
		
		verify(service, only()).signup(sign);
	}
}
