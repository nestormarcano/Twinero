package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.util.Objects;

public class WithdrawReqDTO
{
	private WithdrawDTO withdrawDTO;
	private String sessionID;

	public WithdrawReqDTO ()
	{
	}

	public WithdrawReqDTO (	WithdrawDTO theDeposit,
									String theSessionID )
	{
		this.withdrawDTO = theDeposit;
		this.sessionID = theSessionID;
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

		final WithdrawReqDTO other = (WithdrawReqDTO) obj;
		return (Objects.equals(this.withdrawDTO, other.withdrawDTO) && Objects.equals(this.sessionID, other.sessionID));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.withdrawDTO, this.sessionID);
	}

	/**
	 * @return the withdrawDTO
	 */
	public WithdrawDTO getWithdrawDTO ()
	{
		return this.withdrawDTO;
	}

	/**
	 * @param newWithdraw the withdrawDTO to set
	 */
	public void setWithdrawDTO (WithdrawDTO newWithdraw )
	{
		this.withdrawDTO = newWithdraw;
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
