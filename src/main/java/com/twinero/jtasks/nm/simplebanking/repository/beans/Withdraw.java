package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.util.Objects;

public class Withdraw extends ClientBaseObject
{
	private long withdrawID;
	
	public Withdraw()
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
			return (Objects.equals(this.withdrawID, other.withdrawID));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.withdrawID);
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
}
