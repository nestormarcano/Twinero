package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.twinero.jtasks.nm.simplebanking.utils.MoneySerializer;

public class AccountBalance extends ClientBaseObject
{
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSZ";
	public static final String TIMEZONE = "GMT-4";
	
	private long id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT, timezone = TIMEZONE)
	private Date date;
	
   @JsonSerialize(using = MoneySerializer.class)
	private BigDecimal available;
	
   @JsonSerialize(using = MoneySerializer.class)
	private BigDecimal total;
	
   @JsonSerialize(using = MoneySerializer.class)
	private BigDecimal deferred;
	
   @JsonSerialize(using = MoneySerializer.class)
	private BigDecimal blocked;
	
	public AccountBalance()
	{
		
	}
	
	public AccountBalance(long theId)
	{
		this.id = theId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj )
	{
		if (super.equals(obj))
		{
			final AccountBalance other = (AccountBalance) obj;
			return (Objects.equals(this.id, other.id)
					&& Objects.equals(this.date, other.date)
					&& Objects.equals(this.available, other.available)
					&& Objects.equals(this.total, other.total)
					&& Objects.equals(this.deferred, other.deferred)
					&& Objects.equals(this.blocked, other.blocked));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.id, this.date,
				this.available, this.total, this.deferred, this.blocked);
	}

	/**
	 * @return the id
	 */
	public long getId ()
	{
		return this.id;
	}

	/**
	 * @param newId the id to set
	 */
	public void setId (long newId )
	{
		this.id = newId;
	}
	
	/**
	 * @return the date
	 */
	public Date getDate ()
	{
		return this.date;
	}

	/**
	 * @param newDate the date to set
	 */
	public void setDate (Date newDate )
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
	 * @param blocked the blocked to set
	 */
	public void setBlocked (BigDecimal newBlocked )
	{
		this.blocked = newBlocked;
	}
}
