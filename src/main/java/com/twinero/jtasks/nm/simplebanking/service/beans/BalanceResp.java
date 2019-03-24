package com.twinero.jtasks.nm.simplebanking.service.beans;

import java.util.Objects;

public class BalanceResp
{
	public static enum SessionStatus
	{
		UNDEFINED, OK, EXPIRED, DOES_NOT_EXIST, UNAUTHORIZED;
	}

	private SessionStatus sessionStatus;
	private Balance balance;

	public BalanceResp ()
	{
		this.sessionStatus = SessionStatus.UNDEFINED;
	}

	public BalanceResp (	Balance theBalance,
											SessionStatus theStatus )
	{
		this.balance = theBalance;
		this.sessionStatus = theStatus;
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

		final BalanceResp other = (BalanceResp) obj;
		return (Objects.equals(this.sessionStatus, other.sessionStatus) && Objects.equals(this.balance, other.balance));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.sessionStatus, this.balance);
	}

	/**
	 * @return the balance.
	 */
	public Balance getBalance ()
	{
		return this.balance;
	}

	/**
	 * @param newBalance the balance to set.
	 */
	public void setBalance (Balance newBalance )
	{
		this.balance = newBalance;
	}

	/**
	 * @return the sessionStatus.
	 */
	public SessionStatus getSessionStatus ()
	{
		return this.sessionStatus;
	}

	/**
	 * @param newSessionStatus the sessionStatus to set.
	 */
	public void setSessionStatus (SessionStatus newSessionStatus )
	{
		this.sessionStatus = newSessionStatus;
	}
}
