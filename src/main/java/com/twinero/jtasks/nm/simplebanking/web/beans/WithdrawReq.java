package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.util.Objects;

import com.twinero.jtasks.nm.simplebanking.repository.beans.Withdraw;

public class WithdrawReq
{
	private Withdraw withdraw;
	private String sessionID;
	
	public WithdrawReq()
	{	
	}
	
	public WithdrawReq(Withdraw theDeposit, String theSessionID)
	{
		this.withdraw = theDeposit;
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

		final WithdrawReq other = (WithdrawReq) obj;
		return (Objects.equals(this.withdraw, other.withdraw) && Objects.equals(this.sessionID, other.sessionID));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.withdraw, this.sessionID);
	}

	/**
	 * @return the withdraw
	 */
	public Withdraw getWithdraw ()
	{
		return this.withdraw;
	}

	/**
	 * @param newWithdraw the withdraw to set
	 */
	public void setWithdraw (Withdraw newWithdraw )
	{
		this.withdraw = newWithdraw;
	}

	/**
	 * @return the sessionID
	 */
	public String getSessionID ()
	{
		return this.sessionID;
	}

	/**
	 * @param newSessionID the sessionID to set
	 */
	public void setSessionID (String newSessionID )
	{
		this.sessionID = newSessionID;
	}
}
