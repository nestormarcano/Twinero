package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import javax.validation.constraints.NotBlank;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.twinero.jtasks.nm.simplebanking.repository.beans.ClientBaseObject;
import com.twinero.jtasks.nm.simplebanking.utils.MoneySerializer;

public class DepositDTO extends ClientBaseObject
{
	private long movementID;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal amount;

	@NonNull
	@NotBlank
	private String dateTime;
	
	private String reference;

	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------- Default constructor DepositDTO
	/**
	 * Constructor by default.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public DepositDTO ()
	{
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------- Constructor DepositDTO with the deposit id
	/**
	 * Constructor using the deposit id.
	 * @param theMovementID The deposit id.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public DepositDTO ( long theMovementID )
	{
		this.movementID = theMovementID;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------- getTime
	/**
	 * Extended getter to get the dateTime formatted accord a dateFormat and a timeZone.
	 *  
	 * @param dateFormat The SimpleDateFormat object to be used in parsing the date.
	 * @param timezone The dateTime zone to be used in parsing the date.
	 * 
	 * @return The dateTime.
	 * @throws ParseException Object with the error data.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public Date getDateTime (	SimpleDateFormat dateFormat,
										String timezone )
		throws ParseException
	{
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		return dateFormat.parse(this.dateTime);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------- setTime
	/**
	 * Extended setter to set the dateTime according a dateFormat and a timeZone.
	 * 
	 * @param newDateTime The dateTime to be set.
	 * @param dateFormat The dateFormat used to set the date.
	 * @param timezone The timeZone used to set the date. 
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public void setDateTime (	Date newDateTime,
										SimpleDateFormat dateFormat,
										String timezone )
	{
		if (newDateTime == null)
		{
			this.dateTime = null;
			return;
		}

		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		this.dateTime = dateFormat.format(newDateTime);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------- equals
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public boolean equals (Object obj )
	{
		if (this == obj)
			return true;

		if ((obj == null) || !this.getClass().equals(obj.getClass()))
			return false;

		if (super.equals(obj))
		{
			final DepositDTO other = (DepositDTO) obj;
			return (Objects.equals(this.movementID, other.movementID)
					&& Objects.equals(this.amount, other.amount)
					&& Objects.equals(this.dateTime, other.dateTime)
					&& Objects.equals(this.reference, other.reference));
		}

		return false;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------------------------- hashCode
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.movementID, this.amount, this.dateTime, this.reference);
	}

	/**
	 * @return the movementID
	 */
	public long getMovementID ()
	{
		return this.movementID;
	}

	/**
	 * @param newMovementID the movementID to set
	 */
	public void setMovementID (long newMovementID )
	{
		this.movementID = newMovementID;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount ()
	{
		return this.amount;
	}

	/**
	 * @param newAmount the amount to set
	 */
	public void setAmount (BigDecimal newAmount )
	{
		this.amount = newAmount;
	}

	/**
	 * @return the dateTime
	 */
	public String getDateTime ()
	{
		return this.dateTime;
	}

	/**
	 * @param newDateTime the dateTime to set
	 */
	public void setDateTime (String newDateTime )
	{
		this.dateTime = newDateTime;
	}

	/**
	 * @return the reference
	 */
	public String getReference ()
	{
		return this.reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference (String reference )
	{
		this.reference = reference;
	}
}
