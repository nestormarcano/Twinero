package com.twinero.jtasks.nm.simplebanking.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Util
{
	// ----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------
	/*
	 * Converts a java object into JSON representation.
	 */
	// ----------------------------------------------------------------------------------------------------------------
	public static String asJsonString (final Object obj )
	{
		try
		{
			ObjectMapper objMapper = new ObjectMapper();
			objMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
			return objMapper.writeValueAsString(obj);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------
	/*
	 * Converts a java object into JSON representation.
	 */
	// ----------------------------------------------------------------------------------------------------------------
	public static String asJsonString (final Object obj, String dateFormatPattern )
	{
		try
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatPattern);
			ObjectMapper objMapper = new ObjectMapper();
			objMapper.setDateFormat(simpleDateFormat);
			objMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
			return objMapper.writeValueAsString(obj);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	
	// ----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------
	/*
	 * Serializes a java object into byte[].
	 */
	// ----------------------------------------------------------------------------------------------------------------
	public static byte[] serialize (Object obj )
		throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
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

		Matcher matcher = pattern.matcher(email);
		return matcher.find();
	}
}
