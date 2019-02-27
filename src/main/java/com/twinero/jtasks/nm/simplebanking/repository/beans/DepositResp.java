package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.util.Objects;

public class DepositResp
{
	public static enum Status
	{
		OK, SESSION_EXPIRED, SESSION_DOES_NOT_EXIST, INVALID_CLIENT, SERVER_ERROR;
	}

	private Status status;
	private Deposit deposit;
	
	public DepositResp ()
	{
	}

	public DepositResp (	Deposit theDeposit,
								Status theStatus )
	{
		this.deposit = theDeposit;
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

		final DepositResp other = (DepositResp) obj;
		return (Objects.equals(this.status, other.status) && Objects.equals(this.deposit, other.deposit));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.status, this.deposit);
	}
	
	/**
	 * @return the status
	 */
	public Status getStatus ()
	{
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus (Status status )
	{
		this.status = status;
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
}
