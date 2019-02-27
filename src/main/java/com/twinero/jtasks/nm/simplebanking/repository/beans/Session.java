package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.util.Objects;

public class Session extends ClientBaseObject
{
	public static enum Status
	{
		UNDEFINED, OK, UNAUTHORIZED,
		EXPIRED, NOT_EXISTS, INTERNAL_ERROR
	}

	private String sessionID;
	private String email;
	private String password;
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

	public Session (	String theEmail,
							String thePassword )
	{
		this.email = theEmail;
		this.password = thePassword;
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
					&& Objects.equals(this.email, other.email)
					&& Objects.equals(this.password, other.password)
					&& Objects.equals(this.sessionStatus, other.sessionStatus));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.sessionID,
				this.email, this.password, this.sessionStatus, this.getClientID());
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
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail (String email )
	{
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword ()
	{
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword (String password )
	{
		this.password = password;
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
