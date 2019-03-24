package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.util.Objects;

public class SignRespDTO
{
	public static enum Status
	{
		UNDEFINED_STATUS, OK, ALREADY_EXISTS, BAD_REQUEST, SERVER_ERROR;
	}

	private long signID;
	private String email;
	private Status status;

	public SignRespDTO ()
	{
		this.status = Status.UNDEFINED_STATUS;
	}

	public SignRespDTO ( Status theStatus )
	{
		this.status = theStatus;
	}

	/**
	 * Constructor with email and password.
	 * 
	 * @param theEmail Customer's email.
	 */
	public SignRespDTO ( String theEmail )
	{
		this.email = theEmail;
		this.status = Status.UNDEFINED_STATUS;
	}

	@Override
	public String toString ()
	{
		return String.format("SignDAO[signID=%d, email='%s', status='%s']", signID, email, status);
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

		final SignRespDTO other = (SignRespDTO) obj;
		return (Objects.equals(this.signID, other.signID) && Objects.equals(this.email, other.email)
				&& Objects.equals(this.status, other.status));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.signID, this.email, this.status);
	}

	/**
	 * @return the signID
	 */
	public long getSignID ()
	{
		return this.signID;
	}

	/**
	 * @param newSignID the signID to set
	 */
	public void setSignID (long newSignID )
	{
		this.signID = newSignID;
	}

	/**
	 * @return the email
	 */
	public String getEmail ()
	{
		return email;
	}

	/**
	 * @param email the email to set.
	 */
	public void setEmail (String email )
	{
		this.email = email;
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