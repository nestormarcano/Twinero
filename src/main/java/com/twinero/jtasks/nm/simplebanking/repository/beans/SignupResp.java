package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.util.Objects;

public class SignupResp
{
	public static enum Status
	{
		UNDEFINED_STATUS, OK, ALREADY_EXISTS, BAD_REQUEST, SERVER_ERROR;
	}

	private Status status;

	public SignupResp ()
	{
		this.status = Status.UNDEFINED_STATUS;
	}

	public SignupResp ( Status theStatus )
	{
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

		final SignupResp other = (SignupResp) obj;
		return (Objects.equals(this.status, other.status));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.status);
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
