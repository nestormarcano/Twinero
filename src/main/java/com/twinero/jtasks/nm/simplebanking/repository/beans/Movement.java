package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twinero.jtasks.nm.simplebanking.utils.MoneySerializer;

public class Movement
{
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSZ";
	public static final String TIMEZONE = "GMT-4";
	
	private long id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT, timezone = TIMEZONE)
	private Date date;
	
	private String reference;
	private String description;
	
   @JsonSerialize(using = MoneySerializer.class)
	private BigDecimal deposit;
	
   @JsonSerialize(using = MoneySerializer.class)
	private BigDecimal withdraw;
	
   @JsonSerialize(using = MoneySerializer.class)
	private BigDecimal tax;
	
	public Movement()
	{
	}
	
	public Movement(long theId)
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
			final Movement other = (Movement) obj;
			return (Objects.equals(this.id, other.id)
					&& Objects.equals(this.date, other.date)
					&& Objects.equals(this.reference, other.reference)
					&& Objects.equals(this.description, other.description)
					&& Objects.equals(this.deposit, other.deposit)
					&& Objects.equals(this.withdraw, other.withdraw)
					&& Objects.equals(this.tax, other.tax));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.id, this.date,
				this.reference, this.description, this.deposit, this.withdraw, this.tax);
	}

	/**
	 * @return the id
	 */
	public long getId ()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId (long id )
	{
		this.id = id;
	}

	/**
	 * @return the date
	 */
	public Date getDate ()
	{
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate (Date date )
	{
		this.date = date;
	}

	/**
	 * @return the reference
	 */
	public String getReference ()
	{
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference (String reference )
	{
		this.reference = reference;
	}

	/**
	 * @return the description
	 */
	public String getDescription ()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription (String description )
	{
		this.description = description;
	}

	/**
	 * @return the deposit
	 */
	public BigDecimal getDeposit ()
	{
		return deposit;
	}

	/**
	 * @param deposit the deposit to set
	 */
	public void setDeposit (BigDecimal deposit )
	{
		this.deposit = deposit;
	}

	/**
	 * @return the withdraw
	 */
	public BigDecimal getWithdraw ()
	{
		return withdraw;
	}

	/**
	 * @param withdraw the withdraw to set
	 */
	public void setWithdraw (BigDecimal withdraw )
	{
		this.withdraw = withdraw;
	}

	/**
	 * @return the tax
	 */
	public BigDecimal getTax ()
	{
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax (BigDecimal tax )
	{
		this.tax = tax;
	}
}
