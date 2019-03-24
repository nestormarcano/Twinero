package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.util.Objects;

import com.twinero.jtasks.nm.simplebanking.repository.beans.ClientBaseObject;

public class SessionRespDTO extends ClientBaseObject
{
	public static enum Status
	{
		UNDEFINED, OK, UNAUTHORIZED, EXPIRED, NOT_EXISTS
	}

	private String sessionID;
	private String email;
	private Status sessionStatus;

	public SessionRespDTO ()
	{
		this.sessionStatus = Status.UNDEFINED;
	}

	public SessionRespDTO ( String theSessionID )
	{
		this.sessionID = theSessionID;
		this.sessionStatus = Status.UNDEFINED;
	}

	public SessionRespDTO (	String theEmail,
							String thePassword )
	{
		this.email = theEmail;
		this.sessionStatus = Status.UNDEFINED;
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
		
		if (super.equals(obj))
		{
			final SessionRespDTO other = (SessionRespDTO) obj;
			return (Objects.equals(this.sessionID, other.sessionID)
					&& Objects.equals(this.email, other.email)
					&& Objects.equals(this.sessionStatus, other.sessionStatus));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.sessionID,
				this.email, this.sessionStatus, this.getClientID());
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
	public void setSessionID (String newSessionID )
	{
		this.sessionID = newSessionID;
	}

	/**
	 * @return the email
	 */
	public String getEmail ()
	{
		return this.email;
	}

	/**
	 * @param newEmail the email to set
	 */
	public void setEmail (String newEmail )
	{
		this.email = newEmail;
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
