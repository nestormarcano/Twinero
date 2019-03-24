package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.util.Objects;

public class WithdrawRespDTO
{
	public static enum SessionStatus
	{
		UNDEFINED, OK, EXPIRED, DOES_NOT_EXIST, UNAUTHORIZED;
	}

	private SessionStatus sessionStatus;
	private WithdrawDTO withdraw;

	public WithdrawRespDTO ()
	{
	}

	public WithdrawRespDTO (	WithdrawDTO theWithdraw,
										SessionStatus theStatus )
	{
		this.withdraw = theWithdraw;
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

		final WithdrawRespDTO other = (WithdrawRespDTO) obj;
		return (Objects.equals(this.sessionStatus, other.sessionStatus) && Objects.equals(this.withdraw, other.withdraw));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.sessionStatus, this.withdraw);
	}

	/**
	 * @return the sessionStatus
	 */
	public SessionStatus getSessionStatus ()
	{
		return sessionStatus;
	}

	/**
	 * @param newStatus the sessionStatus to set
	 */
	public void setSessionStatus (SessionStatus newStatus )
	{
		this.sessionStatus = newStatus;
	}

	/**
	 * @return the withdraw
	 */
	public WithdrawDTO getWithdraw ()
	{
		return this.withdraw;
	}

	/**
	 * @param newWithdraw the withdraw to set
	 */
	public void setWithdraw (WithdrawDTO newWithdraw )
	{
		this.withdraw = newWithdraw;
	}
}
