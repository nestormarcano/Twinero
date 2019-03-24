package com.twinero.jtasks.nm.simplebanking.service.beans;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import com.twinero.jtasks.nm.simplebanking.repository.beans.ClientBaseObject;

public class Statement extends ClientBaseObject
{
	private long id;
	private Date since;
	private Date  until;
	private String accountNumber;
	private Set<Movement> movements;
	
	public Statement()
	{	
	}
	
	public Statement(long theId)
	{
		this.id = theId;
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
			final Statement other = (Statement) obj;
			return (Objects.equals(this.id, other.id)
					&& Objects.equals(this.since, other.since)
					&& Objects.equals(this.until, other.until)
					&& Objects.equals(this.accountNumber, other.accountNumber)
					&& Objects.equals(this.movements, other.movements));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.id, this.since,
				this.until, this.accountNumber, this.movements);
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
	 * @return the since
	 */
	public Date getSince ()
	{
		return this.since;
	}

	/**
	 * @param newSince the since to set
	 */
	public void setSince (Date newSince )
	{
		this.since = newSince;
	}

	/**
	 * @return the until
	 */
	public Date getUntil ()
	{
		return this.until;
	}

	/**
	 * @param newUntil the until to set
	 */
	public void setUntil (Date newUntil )
	{
		this.until = newUntil;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber ()
	{
		return this.accountNumber;
	}

	/**
	 * @param newAccountNumber the accountNumber to set
	 */
	public void setAccountNumber (String newAccountNumber )
	{
		this.accountNumber = newAccountNumber;
	}

	/**
	 * @return the movements
	 */
	public Set<Movement> getMovements ()
	{
		return this.movements;
	}

	/**
	 * @param newMovements the movements to set
	 */
	public void setMovements (Set<Movement> newMovements )
	{
		this.movements = newMovements;
	}
}
