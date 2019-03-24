package com.twinero.jtasks.nm.simplebanking.service.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.twinero.jtasks.nm.simplebanking.repository.beans.ClientBaseObject;

/**
 * Implements the Balance domain object for the Simple Bank application.
 * @author Nestor Marcano.
 */
// --------------------------------------------------------------------------------------------------------------------
public class Balance extends ClientBaseObject
{
	private Date date;
	private BigDecimal available;
	private BigDecimal total;
	private BigDecimal deferred;
	private BigDecimal blocked;
	
	public Balance()
	{
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
			final Balance other = (Balance) obj;
			return (Objects.equals(this.date, other.date)
					&& Objects.equals(this.available, other.available)
					&& Objects.equals(this.total, other.total)
					&& Objects.equals(this.deferred, other.deferred)
					&& Objects.equals(this.blocked, other.blocked));
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.date,
				this.available, this.total, this.deferred, this.blocked);
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
	 * @param newBlocked the blocked to set
	 */
	public void setBlocked (BigDecimal newBlocked )
	{
		this.blocked = newBlocked;
	}
}
