package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.util.Objects;

public class Deposit extends ClientBaseObject
{
	private long depositID;
	
	public Deposit()
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
			return (Objects.equals(this.depositID, other.depositID));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.depositID);
	}
	
	/**
	 * @return the depositID
	 */
	public long getDepositID ()
	{
		return this.depositID;
	}

	/**
	 * @param newId the depositID to set
	 */
	public void setDepositID (long newId )
	{
		this.depositID = newId;
	}
}
