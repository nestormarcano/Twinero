package com.twinero.jtasks.nm.simplebanking.repository;

import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.beans.SignupResp;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;

@Repository
public interface SimpleBankRepository
{
	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------- signup
	/**
	 * Performs a sign-up in the system.
	 * 
	 * @param sign The signing data.
	 * 
	 * @return Object with the response data.
	 * @throws SimpleBankServiceException Object indicating an service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public SignupResp signup (Sign sign )
		throws SimpleBankServiceException;
}
