package com.twinero.jtasks.nm.simplebanking.service.beans;

import java.util.Objects;

/**
 * Contains the response from the getMovements method of the service.<br>
 * The respond consists of a statement and a session status.
 * 
 * @author Nestor Marcano.
 */
// --------------------------------------------------------------------------------------------------------------------
public class MovementResp
{
	public static enum SessionStatus
	{
		UNDEFINED, OK, EXPIRED, DOES_NOT_EXIST, UNAUTHORIZED;
	}

	private SessionStatus sessionStatus;
	private Movement movement;

	public MovementResp ()
	{
		this.sessionStatus = SessionStatus.UNDEFINED;
	}

	public MovementResp (	Movement theMovement,
										SessionStatus theStatus )
	{
		this.movement = theMovement;
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

		final MovementResp other = (MovementResp) obj;
		return (Objects.equals(this.sessionStatus, other.sessionStatus)
				&& Objects.equals(this.movement, other.movement));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.sessionStatus, this.movement);
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
	 * @return the movement
	 */
	public Movement getMovement ()
	{
		return this.movement;
	}

	/**
	 * @param newMovement the movement to set
	 */
	public void setMovement (Movement newMovement )
	{
		this.movement = newMovement;
	}
}
