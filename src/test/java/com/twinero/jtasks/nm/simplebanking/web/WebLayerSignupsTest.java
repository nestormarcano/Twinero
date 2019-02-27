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
import com.twinero.jtasks.nm.simplebanking.service.beans.SignReq;
import com.twinero.jtasks.nm.simplebanking.service.beans.SignupResp;
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
public class WebLayerSignupsTest
{
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SimpleBankService service;

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------- shouldSignupAndReturnOK
	/**
	 * Performs a valid Sign up.
	 * 
	 * @throws Exception Data Error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldSignupAndReturnOK ()
		throws Exception
	{
		String email = "nestor.marcano@gmail.com";
		String password = "123456";
		
		SignReq signReq = new SignReq(email, password);
		
		SignupResp signup = new SignupResp(SignupResp.Status.OK);
		when(service.signup(signReq)).thenReturn(signup);

		this.mockMvc
				.perform(
						post("/simpleBanking/signups")
								.contentType(MediaType.APPLICATION_JSON)
								.content(Util.asJsonString(signReq))
								.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString(Util.asJsonString(signup))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("signups/signup"))
				.andReturn();
		
		verify(service, only()).signup(signReq);
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
		SignReq signReq = new SignReq(email, password);

		SignupResp signup = new SignupResp(SignupResp.Status.ALREADY_EXISTS);
		when(service.signup(signReq)).thenReturn(signup);

		this.mockMvc
				.perform(post("/simpleBanking/signups")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(signReq))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isConflict())
				.andExpect(content().string(containsString(Util.asJsonString(signup))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("signups/notSignup"))
				.andReturn();
		
		verify(service, only()).signup(signReq);
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
		String password = "123 456";
		SignReq signReq = new SignReq(email, password);

		SignupResp signup = new SignupResp(SignupResp.Status.BAD_REQUEST);
		when(service.signup(signReq)).thenReturn(signup);
		
		this.mockMvc
				.perform(post("/simpleBanking/signups")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(signReq))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString(Util.asJsonString(signup))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("signups/notSignupMalformedEmail"))
				.andReturn();
		
		verify(service, times(0)).signup(signReq);
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
		SignReq signReq = new SignReq(email, password);

		SignupResp signup = new SignupResp(SignupResp.Status.SERVER_ERROR);
		when(service.signup(signReq)).thenThrow(SimpleBankServiceException.class);

		this.mockMvc
				.perform(post("/simpleBanking/signups")
						.contentType(MediaType.APPLICATION_JSON)
						.content(Util.asJsonString(signReq))
						.characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content().string(containsString(Util.asJsonString(signup))))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andDo(document("signups/notSignupServerError"))
				.andReturn();
		
		verify(service, only()).signup(signReq);
	}
}
