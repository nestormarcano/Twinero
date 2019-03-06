package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twinero.jtasks.nm.simplebanking.utils.MoneySerializer;

public class Deposit extends ClientBaseObject
{
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSZ";
	public static final String TIMEZONE = "GMT-4";
	
	private long depositID;
	
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal mount;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT, timezone = TIMEZONE)
	private Date time;
	
	private String reference;

	public Deposit ()
	{
	}

	public Deposit ( long theDepositID )
	{
		this.depositID = theDepositID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj )
	{
		if (super.equals(obj))
		{
			final Deposit other = (Deposit) obj;
			return (Objects.equals(this.depositID, other.depositID)
					&& Objects.equals(this.mount, other.mount)
					&& Objects.equals(this.time, other.time)
					&& Objects.equals(this.reference, other.reference));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.depositID, this.mount, this.time, this.reference);
	}

	/**
	 * @return the depositID
	 */
	public long getDepositID ()
	{
		return this.depositID;
	}

	/**
	 * @param newDepositID the depositID to set
	 */
	public void setDepositID (long newDepositID )
	{
		this.depositID = newDepositID;
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
	 * @param time the time to set
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
	 * @param reference the reference to set
	 */
	public void setReference (String reference )
	{
		this.reference = reference;
	}
}
