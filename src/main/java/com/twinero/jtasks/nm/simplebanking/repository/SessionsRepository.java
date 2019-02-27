package com.twinero.jtasks.nm.simplebanking.repository;

import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.beans.SesionStatus;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;

@Repository
public interface SessionsRepository
{
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------- get
	/**
	 * Performs a session verification.
	 * 
	 * @param sessionID The identification of the session.
	 * 
	 * @return The status of the session.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public SesionStatus get (String sessionID )
		throws SimpleBankServiceException;
	
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------- add
	/**
	 * Adds a login in the system.
	 * 
	 * @param sign The login data.
	 * 
	 * @return Object with the response data.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public Session add (Sign sign )
		throws SimpleBankServiceException;
}
