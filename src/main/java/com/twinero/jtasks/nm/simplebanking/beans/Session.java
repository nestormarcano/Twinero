package com.twinero.jtasks.nm.simplebanking.beans;

import java.util.Objects;

public class Session extends ClientBaseObject
{
	private String sessionID;

	public Session ()
	{
	}

	public Session (	String theSessionID )
	{
		this.sessionID = theSessionID;
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
			return (Objects.equals(this.sessionID, other.sessionID));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.sessionID, this.getClientID());
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
}
