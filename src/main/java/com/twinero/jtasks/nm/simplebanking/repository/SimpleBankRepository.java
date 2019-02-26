package com.twinero.jtasks.nm.simplebanking.repository;

import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.beans.AccountBalanceResp;
import com.twinero.jtasks.nm.simplebanking.beans.AccountStatementResp;
import com.twinero.jtasks.nm.simplebanking.beans.Session;
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
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public SignupResp signup (Sign sign )
		throws SimpleBankServiceException;

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------- login
	/**
	 * Performs a login in the system.
	 * 
	 * @param sign The login data.
	 * 
	 * @return Object with the response data.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public Session login (Sign sign )
		throws SimpleBankServiceException;

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------- getAccountBalance
	/**
	 * Gets the account balances for a client.
	 * 
	 * @param clientID The identification of the client.
	 * @param sessionID The identification of the session.
	 * 
	 * @return The account balance.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public AccountBalanceResp getAccountBalance (long clientID,
																String sessionID )
		throws SimpleBankServiceException;

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------- getAccountStatement
	/**
	 * Gets the account statement for a client.
	 * 
	 * @param clientID The identification of the client.
	 * @param sessionID The identification of the session.
	 * 
	 * @return The account statement.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public AccountStatementResp getAccountStatement (	long clientID,
																		String sessionID )
		throws SimpleBankServiceException;
}
