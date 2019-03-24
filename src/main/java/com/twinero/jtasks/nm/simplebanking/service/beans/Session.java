package com.twinero.jtasks.nm.simplebanking.service.beans;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.twinero.jtasks.nm.simplebanking.repository.beans.ClientBaseObject;

/**
 * Implements the Session domain object for the Simple Bank application.
 * @author Nestor Marcano.
 */
// --------------------------------------------------------------------------------------------------------------------
public class Session extends ClientBaseObject
{
	public static final String emailRegExp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*"
			+ "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
	
	public static enum Status
	{
		UNDEFINED, OK, UNAUTHORIZED, EXPIRED, NOT_EXISTS
	}

	private String sessionID;
	
	@NotBlank
	@Pattern(regexp = emailRegExp)
	private String email;
	
	@NotBlank
	@NotNull
	private String password;
	
	private Status sessionStatus;

	public Session ()
	{
		this.sessionStatus = Status.UNDEFINED;
	}

	public Session ( String theSessionID )
	{
		this.sessionID = theSessionID;
		this.sessionStatus = Status.UNDEFINED;
	}

	public Session (	String theEmail,
							String thePassword )
	{
		this.email = theEmail;
		this.password = thePassword;
		this.sessionStatus = Status.UNDEFINED;
	}
	
	public Session ( Status theStatus )
	{
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
	 * @return the password
	 */
	public String getPassword ()
	{
		return this.password;
	}

	/**
	 * @param newPassword the password to set
	 */
	public void setPassword (String newPassword )
	{
		this.password = newPassword;
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
