package com.twinero.jtasks.nm.simplebanking.service.beans;

import java.util.Objects;

import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountBalance;

public class AccountBalanceResp
{
	public static enum Status
	{
		OK, SESSION_EXPIRED, SESSION_DOES_NOT_EXIST, SERVER_ERROR, UNAUTHORIZED;
	}

	private Status status;
	private AccountBalance balance;

	public AccountBalanceResp ()
	{
	}

	public AccountBalanceResp (	AccountBalance theBalance,
											Status theStatus )
	{
		this.balance = theBalance;
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

		final AccountBalanceResp other = (AccountBalanceResp) obj;
		return (Objects.equals(this.status, other.status) && Objects.equals(this.balance, other.balance));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.status, this.balance);
	}

	/**
	 * @return the balance.
	 */
	public AccountBalance getBalance ()
	{
		return this.balance;
	}

	/**
	 * @param newBalance the balance to set.
	 */
	public void setBalance (AccountBalance newBalance )
	{
		this.balance = newBalance;
	}

	/**
	 * @return the status.
	 */
	public Status getStatus ()
	{
		return this.status;
	}

	/**
	 * @param newStatus the status to set.
	 */
	public void setStatus (Status newStatus )
	{
		this.status = newStatus;
	}
}
