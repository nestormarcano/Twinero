package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.util.Objects;

import com.twinero.jtasks.nm.simplebanking.repository.beans.Deposit;

public class DepositReq
{
	private Deposit deposit;
	private String sessionID;
	
	public DepositReq()
	{	
	}
	
	public DepositReq(Deposit theDeposit, String theSessionID)
	{
		this.deposit = theDeposit;
		this.sessionID = theSessionID;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj )
	{
		if (super.equals(obj))
			return true;

		if ((obj == null) || !this.getClass().equals(obj.getClass()))
		{ return false; }

		final DepositReq other = (DepositReq) obj;
		return (Objects.equals(this.deposit, other.deposit) && Objects.equals(this.sessionID, other.sessionID));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.deposit, this.sessionID);
	}

	/**
	 * @return the deposit
	 */
	public Deposit getDeposit ()
	{
		return deposit;
	}

	/**
	 * @param deposit the deposit to set
	 */
	public void setDeposit (Deposit deposit )
	{
		this.deposit = deposit;
	}

	/**
	 * @return the sessionID
	 */
	public String getSessionID ()
	{
		return sessionID;
	}

	/**
	 * @param sessionID the sessionID to set
	 */
	public void setSessionID (String sessionID )
	{
		this.sessionID = sessionID;
	}
}
