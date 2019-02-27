package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.util.Objects;

public class Session extends ClientBaseObject
{
	public static enum Status
	{
		UNDEFINED, OK, UNAUTHORIZED
	}

	private String sessionID;
	private Status sessionStatus;

	public Session ()
	{
		sessionStatus = Status.UNDEFINED;
	}

	public Session ( String theSessionID )
	{
		this.sessionID = theSessionID;
		sessionStatus = Status.UNDEFINED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj )
	{
		if (super.equals(obj))
		{
			final Session other = (Session) obj;
			return (Objects.equals(this.sessionID, other.sessionID)
					&& Objects.equals(this.sessionStatus, other.sessionStatus));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.sessionID, this.sessionStatus, this.getClientID());
	}

	/**
	 * @return the sessionID
	 */
	public String getSessionID ()
	{
		return this.sessionID;
	}

	/**
	 * @param newSessionID the sessionID to set.
	 */
	public void setId (String newSessionID )
	{
		this.sessionID = newSessionID;
	}

	/**
	 * @return the sessionStatus
	 */
	public Status getSessionStatus ()
	{
		return this.sessionStatus;
	}

	/**
	 * @param newStatus the sessionStatus to set
	 */
	public void setSessionStatus (Status newStatus )
	{
		this.sessionStatus = newStatus;
	}
}
