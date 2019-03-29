package com.twinero.jtasks.nm.simplebanking.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.twinero.jtasks.nm.simplebanking.repository.SessionsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.SignupsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SessionDAO;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SignDAO;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.Session;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceLayerSessionsTest
{
	@Autowired
	private SimpleBankService service;

	@Autowired
	@MockBean
	private SessionsRepository sessionsRepository;

	@Autowired
	@MockBean
	private SignupsRepository signupsRepository;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------ shouldLoginAndReturnOK
	/**
	 * Performs a valid login.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldLoginAndReturnOK ()
	{
		try
		{
			long signID = 123L;
			String email = "nestor.marcano@gmail.com";
			String password = "123456";
			String uuidString = "5dd35b40-2410-11e9-b56e-0800200c9a66";
			UUID uuid = UUID.fromString(uuidString);

			Session session = new Session(email, password);

			SignDAO customerFoundSignDAO = new SignDAO(signID);
			customerFoundSignDAO.setEmail(email);
			customerFoundSignDAO.setPassword(password);

			SessionDAO toRepositorySaveSessionDAO = new SessionDAO(customerFoundSignDAO);

			SessionDAO fromRepositorySaveSessionDAO = new SessionDAO(customerFoundSignDAO);
			fromRepositorySaveSessionDAO.setSessionID(uuid);

			Session serviceExpectedSession = new Session(uuidString);
			serviceExpectedSession.setClientID(signID);
			serviceExpectedSession.setEmail(email);
			serviceExpectedSession.setSessionStatus(Session.Status.OK);

			SignDAO signWithoutPass = new SignDAO(email);

			when(signupsRepository.findOne(Example.of(signWithoutPass))).thenReturn(Optional.of(customerFoundSignDAO));
			when(sessionsRepository.save(toRepositorySaveSessionDAO)).thenReturn(fromRepositorySaveSessionDAO);

			Session serviceObtainedSession = service.login(session);

			assertThat(serviceObtainedSession).isEqualTo(serviceExpectedSession);
			verify(signupsRepository, only()).findOne(Example.of(signWithoutPass));
			verify(sessionsRepository, only()).save(toRepositorySaveSessionDAO);
		}

		// Error handling
		// --------------
		catch (Exception ex)
		{
			ex.printStackTrace();
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------- shouldNotLoginBecauseEmailDoesNotExist
	/**
	 * Performs an invalid valid login because the email does not exist.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotLoginBecauseEmailDoesNotExist ()
	{
		try
		{
			String email = "nestor.marcano@gmail.com";
			String password = "123456";

			Session session = new Session(email, password);

			Session serviceExpectedSession = new Session();
			serviceExpectedSession.setEmail(email);
			serviceExpectedSession.setSessionStatus(Session.Status.UNAUTHORIZED);

			SignDAO signWithoutPass = new SignDAO(email);

			when(signupsRepository.findOne(Example.of(signWithoutPass))).thenReturn(Optional.empty());

			Session serviceObtainedSession = service.login(session);

			assertThat(serviceObtainedSession).isEqualTo(serviceExpectedSession);
			verify(signupsRepository, only()).findOne(Example.of(signWithoutPass));
		}

		// Error handling
		// --------------
		catch (Exception ex)
		{
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------- shouldNotLoginBecauseEmailDoesNotMatch
	/**
	 * Performs an invalid valid login because the email does not match.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotLoginBecauseEmailDoesNotMatch ()
	{
		try
		{
			long signID = 123L;
			String email = "nestor.marcano@gmail.com";
			String password = "123456";

			Session session = new Session(email, password);

			Session serviceExpectedSession = new Session();
			serviceExpectedSession.setEmail(email);
			serviceExpectedSession.setSessionStatus(Session.Status.UNAUTHORIZED);

			SignDAO signWithoutPass = new SignDAO(email);

			SignDAO foundSignDAO = new SignDAO(signID);
			foundSignDAO.setEmail(email);
			foundSignDAO.setPassword(password + "abcd");

			when(signupsRepository.findOne(Example.of(signWithoutPass))).thenReturn(Optional.of(foundSignDAO));

			Session serviceObtainedSession = service.login(session);

			assertThat(serviceObtainedSession).isEqualTo(serviceExpectedSession);
			verify(signupsRepository, only()).findOne(Example.of(signWithoutPass));
		}

		// Error handling
		// --------------
		catch (Exception ex)
		{
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------- shouldNotLoginEmailNull
	/**
	 * Performs a not valid login because the sign parameter contains null.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotLoginEmailNull ()
	{
		try
		{
			long signID = 123L;
			String email = null;
			String password = "123456";

			Session session = new Session(email, password);

			SignDAO customerFoundSignDAO = new SignDAO(signID);
			customerFoundSignDAO.setEmail(email);
			customerFoundSignDAO.setPassword(password);

			SessionDAO toRepositorySaveSessionDAO = new SessionDAO(customerFoundSignDAO);
			SignDAO signWithoutPass = new SignDAO(email);

			try
			{
				service.login(session);
			}
			catch (Exception ex)
			{
				verify(signupsRepository, times(0)).findOne(Example.of(signWithoutPass));
				verify(sessionsRepository, times(0)).save(toRepositorySaveSessionDAO);

				clearInvocations(signupsRepository);
				clearInvocations(sessionsRepository);
			}

			verify(signupsRepository, times(0)).findOne(Example.of(signWithoutPass));
			verify(sessionsRepository, times(0)).save(toRepositorySaveSessionDAO);
		}

		// Error handling
		// --------------
		catch (Exception ex)
		{
			assertTrue(false);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------ shouldNotLoginMalformedEmail
	/**
	 * Performs not valid login (malformed email).
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldNotLoginMalformedEmail ()
	{
		try
		{
			long signID = 123L;
			String email = "nestor marcano gmail.com";
			String password = "123456";

			Session session = new Session(email, password);

			SignDAO customerFoundSignDAO = new SignDAO(signID);
			customerFoundSignDAO.setEmail(email);
			customerFoundSignDAO.setPassword(password);

			SessionDAO toRepositorySaveSessionDAO = new SessionDAO(customerFoundSignDAO);
			SignDAO signWithoutPass = new SignDAO(email);

			try
			{
				service.login(session);
			}
			catch (Exception ex)
			{
				verify(signupsRepository, times(0)).findOne(Example.of(signWithoutPass));
				verify(sessionsRepository, times(0)).save(toRepositorySaveSessionDAO);

				clearInvocations(signupsRepository);
				clearInvocations(sessionsRepository);
			}

			verify(signupsRepository, times(0)).findOne(Example.of(signWithoutPass));
			verify(sessionsRepository, times(0)).save(toRepositorySaveSessionDAO);
		}

		// Error handling
		// --------------
		catch (Exception ex)
		{
			assertTrue(false);
		}
	}
}
