package com.twinero.jtasks.nm.simplebanking.utils;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * This class is used to serialize the money amounts with two decimal digits. 
 * @author Nestor Marcano.
 */
// --------------------------------------------------------------------------------------------------------------------
public class MoneySerializer extends JsonSerializer<BigDecimal>
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize (	BigDecimal value,
									JsonGenerator jgen,
									SerializerProvider provider )
		throws IOException,
		JsonProcessingException
	{
		jgen.writeString(value.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
	}
}