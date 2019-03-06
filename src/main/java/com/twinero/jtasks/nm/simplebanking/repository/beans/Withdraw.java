package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twinero.jtasks.nm.simplebanking.utils.MoneySerializer;

public class Withdraw extends ClientBaseObject
{
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSZ";
	public static final String TIMEZONE = "GMT-4";
	
	private long withdrawID;
	
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal mount;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT, timezone = TIMEZONE)
	private Date time;
	
	private String reference;

	public Withdraw ()
	{
	}

	public Withdraw ( long theWithdrawID )
	{
		this.withdrawID = theWithdrawID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj )
	{
		if (super.equals(obj))
		{
			final Withdraw other = (Withdraw) obj;
			return (Objects.equals(this.withdrawID, other.withdrawID)
					&& Objects.equals(this.mount, other.mount)
					&& Objects.equals(this.time, other.time)
					&& Objects.equals(this.reference, other.reference));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.withdrawID, this.mount, this.time, this.reference);
	}

	/**
	 * @return the withdrawID
	 */
	public long getWithdrawID ()
	{
		return this.withdrawID;
	}

	/**
	 * @param newWithdrawID the withdrawID to set
	 */
	public void setWithdrawID (long newWithdrawID )
	{
		this.withdrawID = newWithdrawID;
	}

	/**
	 * @return the mount
	 */
	public BigDecimal getMount ()
	{
		return this.mount;
	}

	/**
	 * @param newMount the mount to set
	 */
	public void setMount (BigDecimal newMount )
	{
		this.mount = newMount;
	}

	/**
	 * @return the time
	 */
	public Date getTime ()
	{
		return this.time;
	}

	/**
	 * @param newTime the time to set
	 */
	public void setTime (Date newTime )
	{
		this.time = newTime;
	}

	/**
	 * @return the reference
	 */
	public String getReference ()
	{
		return this.reference;
	}

	/**
	 * @param newReference the reference to set
	 */
	public void setReference (String newReference )
	{
		this.reference = newReference;
	}
}
