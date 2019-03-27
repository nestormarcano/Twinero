package com.twinero.jtasks.nm.simplebanking.utils;

import java.text.SimpleDateFormat;

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
}
