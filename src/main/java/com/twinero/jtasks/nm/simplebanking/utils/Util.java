package com.twinero.jtasks.nm.simplebanking.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Util
{
	// ----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------
	/*
	 * Converts a Java object into JSON representation.
	 */
	// ----------------------------------------------------------------------------------------------------------------
	public static String asJsonString (final Object obj )
	{
		try
		{
			return new ObjectMapper().writeValueAsString(obj);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	// ---------------------------------------------------------------------------------------------- checksEmailFormat
	// ----------------------------------------------------------------------------------------------------------------
	/*
	 * Checks the email format.
	 */
	// ----------------------------------------------------------------------------------------------------------------
	public static boolean checksEmailFormat (String email )
	{
		Pattern pattern = Pattern.compile(
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

		Matcher mather = pattern.matcher(email);
		return mather.find();
	}
}
