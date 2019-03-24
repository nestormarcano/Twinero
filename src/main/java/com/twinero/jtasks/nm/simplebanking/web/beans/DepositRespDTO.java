package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.util.Objects;

public class DepositRespDTO
{
	public static enum SessionStatus
	{
		UNDEFINED, OK, EXPIRED, DOES_NOT_EXIST, UNAUTHORIZED;
	}

	private SessionStatus sessionStatus;
	private DepositDTO deposit;

	public DepositRespDTO ()
	{
		this.sessionStatus = SessionStatus.UNDEFINED;
	}

	public DepositRespDTO (	DepositDTO theDeposit,
									SessionStatus theStatus )
	{
		this.deposit = theDeposit;
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

		final DepositRespDTO other = (DepositRespDTO) obj;
		return (Objects.equals(this.sessionStatus, other.sessionStatus) && Objects.equals(this.deposit, other.deposit));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.sessionStatus, this.deposit);
	}

	/**
	 * @return the sessionStatus
	 */
	public SessionStatus getSessionStatus ()
	{
		return this.sessionStatus;
	}

	/**
	 * @param newSessionStatus the sessionStatus to set
	 */
	public void setSessionStatus (SessionStatus newSessionStatus )
	{
		this.sessionStatus = newSessionStatus;
	}

	/**
	 * @return the deposit
	 */
	public DepositDTO getDeposit ()
	{
		return this.deposit;
	}

	/**
	 * @param newDepositDTO the deposit to set
	 */
	public void setDeposit (DepositDTO newDepositDTO )
	{
		this.deposit = newDepositDTO;
	}
}
