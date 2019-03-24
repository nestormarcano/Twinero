package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

import org.springframework.lang.NonNull;

public class DepositReqDTO
{
	private DepositDTO depositDTO;
	
	@NonNull
	@NotBlank
	private String sessionID;

	public DepositReqDTO ()
	{
		this.depositDTO = new DepositDTO();
	}

	public DepositReqDTO (	DepositDTO theDeposit,
									String theSessionID )
	{
		this.depositDTO = theDeposit;
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

		final DepositReqDTO other = (DepositReqDTO) obj;
		return (Objects.equals(this.depositDTO, other.depositDTO) && Objects.equals(this.sessionID, other.sessionID));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.depositDTO, this.sessionID);
	}

	/**
	 * @return the depositDTO
	 */
	public DepositDTO getDepositDTO ()
	{
		return this.depositDTO;
	}

	/**
	 * @param newDepositDTO the depositDTO to set
	 */
	public void setDepositDTO (DepositDTO newDepositDTO )
	{
		this.depositDTO = newDepositDTO;
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
