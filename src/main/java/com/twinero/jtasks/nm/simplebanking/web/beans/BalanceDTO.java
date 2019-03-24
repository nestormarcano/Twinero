package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twinero.jtasks.nm.simplebanking.repository.beans.ClientBaseObject;
import com.twinero.jtasks.nm.simplebanking.utils.MoneySerializer;

/**
 * Implements the DTO balance object.
 * @author Nestor Marcano
 */
// --------------------------------------------------------------------------------------------------------------------
public class BalanceDTO extends ClientBaseObject
{
	private String date;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal available;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal total;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal deferred;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal blocked;
	
	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------- default constructor BalanceDTO
	/**
	 * Default constructor
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public BalanceDTO ()
	{
	}

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------- getDate
	/**
	 * Extended getter to get the date formatted accord a dateFormat and a timeZone.
	 *  
	 * @param dateFormat The SimpleDateFormat object to be used in parsing the date.
	 * @param timezone The time zone to be used in parsing the date.
	 * 
	 * @return The date.
	 * @throws ParseException Object with the error data.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public Date getDate (SimpleDateFormat dateFormat, String timezone )
		throws ParseException
	{
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		return dateFormat.parse(this.date);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------- setDate
	/**
	 * Extended setter to set the date according a dateFormat and a timeZone.
	 * 
	 * @param newDate The date to be set.
	 * @param dateFormat The dateFormat used to set the date.
	 * @param timezone The timeZone used to set the date. 
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public void setDate (Date newDate,
	                     SimpleDateFormat dateFormat,
								String timezone )
	{
		if (newDate == null)
		{
			this.date = null;
			return;
		}
		
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		this.date = dateFormat.format(newDate);
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
			final BalanceDTO other = (BalanceDTO) obj;
			return (Objects.equals(this.date, other.date)
					&& Objects.equals(this.available, other.available)
					&& Objects.equals(this.total, other.total)
					&& Objects.equals(this.deferred, other.deferred)
					&& Objects.equals(this.blocked, other.blocked));
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
		return Objects.hash(super.hashCode(), this.date,
				this.available, this.total, this.deferred, this.blocked);
	}

	/**
	 * @return the date
	 */
	public String getDate ()
	{
		return this.date;
	}

	/**
	 * @param newDate the date to set
	 */
	public void setDate (String newDate )
	{
		this.date = newDate;
	}

	/**
	 * @return the available
	 */
	public BigDecimal getAvailable ()
	{
		return this.available;
	}

	/**
	 * @param newAvailable the available to set
	 */
	public void setAvailable (BigDecimal newAvailable )
	{
		this.available = newAvailable;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal ()
	{
		return this.total;
	}

	/**
	 * @param newTotal the total to set
	 */
	public void setTotal (BigDecimal newTotal )
	{
		this.total = newTotal;
	}

	/**
	 * @return the deferred
	 */
	public BigDecimal getDeferred ()
	{
		return this.deferred;
	}

	/**
	 * @param newDeferred the deferred to set
	 */
	public void setDeferred (BigDecimal newDeferred )
	{
		this.deferred = newDeferred;
	}

	/**
	 * @return the blocked
	 */
	public BigDecimal getBlocked ()
	{
		return this.blocked;
	}

	/**
	 * @param newBlocked the blocked to set
	 */
	public void setBlocked (BigDecimal newBlocked )
	{
		this.blocked = newBlocked;
	}
}
