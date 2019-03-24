package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twinero.jtasks.nm.simplebanking.utils.MoneySerializer;

public class MovementDTO
{
	public static enum Type
	{
		DEPOSIT, WITHDRAW;
	}
	
	private long movementID;
	private String date;

	private String reference;
	private String description;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal amount;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal tax;
	
	private Type type;
	
	public MovementDTO ()
	{
	}

	public MovementDTO ( long theMovementID )
	{
		this.movementID = theMovementID;
	}

	public Date getDate (SimpleDateFormat dateFormat,
								String timezone )
		throws ParseException
	{
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		return dateFormat.parse(this.date);
	}

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj )
	{
		if (this == obj)
			return true;

		if ((obj == null) || !this.getClass().equals(obj.getClass()))
			return false;

		final MovementDTO other = (MovementDTO) obj;
		return (Objects.equals(this.movementID, other.movementID)
				&& Objects.equals(this.date, other.date)
				&& Objects.equals(this.reference, other.reference)
				&& Objects.equals(this.description, other.description)
				//&& Objects.equals(this.deposit, other.deposit)
				//&& Objects.equals(this.withdraw, other.withdraw)
				&& Objects.equals(this.amount, other.amount)
				&& Objects.equals(this.tax, other.tax));
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.movementID, this.date,
				//this.reference, this.description, this.deposit, this.withdraw, this.tax);
				this.reference, this.description, this.amount, this.tax);
	}

	/**
	 * @return the movementID
	 */
	public long getMovementID ()
	{
		return movementID;
	}

	/**
	 * @param movementID the movementID to set
	 */
	public void setMovementID (long newMovementID )
	{
		this.movementID = newMovementID;
	}

	/**
	 * @return the date
	 */
	public String getDate ()
	{
		return date;
	}

	/**
	 * @param newDate the date to set
	 */
	public void setDate (String newDate )
	{
		this.date = newDate;
	}

	/**
	 * @return the reference
	 */
	public String getReference ()
	{
		return reference;
	}

	/**
	 * @param newReference the reference to set
	 */
	public void setReference (String newReference )
	{
		this.reference = newReference;
	}

	/**
	 * @return the description
	 */
	public String getDescription ()
	{
		return description;
	}

	/**
	 * @param newDescription the description to set
	 */
	public void setDescription (String newDescription )
	{
		this.description = newDescription;
	}

	/**
	 * @return the deposit
	 */
	/*
	public BigDecimal getDeposit ()
	{
		return deposit;
	}
	*/

	/**
	 * @param deposit the deposit to set
	 */
	/*
	public void setDeposit (BigDecimal deposit )
	{
		this.deposit = deposit;
	}
	*/

	/**
	 * @return the withdraw
	 */
	/*
	public BigDecimal getWithdraw ()
	{
		return withdraw;
	}
	*/

	/**
	 * @param withdraw the withdraw to set
	 */
	/*
	public void setWithdraw (BigDecimal withdraw )
	{
		this.withdraw = withdraw;
	}
	*/

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount ()
	{
		return amount;
	}

	/**
	 * @param newAmount the amount to set
	 */
	public void setAmount (BigDecimal newAmount )
	{
		this.amount = newAmount;
	}
	
	/**
	 * @return the tax
	 */
	public BigDecimal getTax ()
	{
		return tax;
	}

	/**
	 * @param newTax the tax to set
	 */
	public void setTax (BigDecimal newTax )
	{
		this.tax = newTax;
	}

	/**
	 * @return the type
	 */
	public Type getType ()
	{
		return type;
	}

	/**
	 * @param newType the type to set
	 */
	public void setType (Type newType )
	{
		this.type = newType;
	}
}
