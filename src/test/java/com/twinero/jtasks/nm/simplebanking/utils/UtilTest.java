package com.twinero.jtasks.nm.simplebanking.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.twinero.jtasks.nm.simplebanking.web.beans.StatementDTO;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UtilTest
{
	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------- shouldReturnsAsJsonString
	/**
	 * Gets a valid account statement.
	 * Runs with: mvn -Dtest=UtilTest#shouldReturnsAsJsonString test
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Test
	public void shouldReturnsAsJsonString()
	{
		StatementDTO statement = new StatementDTO();
		Util.asJsonString(null);
	}
}
