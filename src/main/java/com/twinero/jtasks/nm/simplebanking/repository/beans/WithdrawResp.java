package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.util.Objects;

public class WithdrawResp
{
	public static enum Status
	{
		OK, SESSION_EXPIRED, SESSION_DOES_NOT_EXIST, INVALID_CLIENT, SERVER_ERROR;
	}

	private Status status;
	private Withdraw withdraw;

	public WithdrawResp ()
	{
	}

	public WithdrawResp (	Withdraw theWithdraw,
									Status theStatus )
	{
		this.withdraw = theWithdraw;
		this.status = theStatus;
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

		final WithdrawResp other = (WithdrawResp) obj;
		return (Objects.equals(this.status, other.status) && Objects.equals(this.withdraw, other.withdraw));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.status, this.withdraw);
	}

	/**
	 * @return the status
	 */
	public Status getStatus ()
	{
		return status;
	}

	/**
	 * @param newStatus the status to set
	 */
	public void setStatus (Status newStatus )
	{
		this.status = newStatus;
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
}
