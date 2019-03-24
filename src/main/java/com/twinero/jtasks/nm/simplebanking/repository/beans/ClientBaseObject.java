package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.util.Objects;

/**
 * Client base object that just contains the client id information.
 * @author Nestor Marcano.
 */
public abstract class ClientBaseObject
{
	private long clientID;

	/**
	 * Default constructor.
	 */
	public ClientBaseObject ()
	{
	}

	/**
	 * Constructor with the client id.
	 * @param theClientID The client id.
	 */
	public ClientBaseObject ( long theClientID )
	{
		this.clientID = theClientID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj )
	{
		final ClientBaseObject other = (ClientBaseObject) obj;
		return (Objects.equals(this.clientID, other.clientID));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.clientID);
	}

	/**
	 * @return the clientID.
	 */
	public long getClientID ()
	{
		return this.clientID;
	}

	/**
	 * @param newClientID the clientID to set.
	 */
	public void setClientID (long newClientID )
	{
		this.clientID = newClientID;
	}
}
