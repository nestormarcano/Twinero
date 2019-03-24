package com.twinero.jtasks.nm.simplebanking.service.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.NotBlank;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twinero.jtasks.nm.simplebanking.repository.beans.ClientBaseObject;
import com.twinero.jtasks.nm.simplebanking.utils.MoneySerializer;

/**
 * Implements the Movement domain object for the Simple Bank application.
 * @author Nestor Marcano.
 */
// --------------------------------------------------------------------------------------------------------------------
public class Movement extends ClientBaseObject
{
	public static enum Type
	{
		DEPOSIT, WITHDRAW;
	}

	private long movementID;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal amount;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal tax;

	private Type type;

	@NonNull
	@NotBlank
	private Date time;

	private String reference;

	private String description;

	public Movement ()
	{
	}

	public Movement ( long theMovementID )
	{
		this.movementID = theMovementID;
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

		if (super.equals(obj))
		{
			final Movement other = (Movement) obj;
			return (Objects.equals(this.movementID, other.movementID)
					&& Objects.equals(this.amount, other.amount)
					&& Objects.equals(this.tax, other.tax)
					&& Objects.equals(this.type, other.type)
					&& Objects.equals(this.time, other.time)
					&& Objects.equals(this.reference, other.reference)
					&& Objects.equals(this.description, other.description));
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(),
				this.movementID, this.amount, this.tax, this.type, this.time, this.reference, this.description);
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
}
