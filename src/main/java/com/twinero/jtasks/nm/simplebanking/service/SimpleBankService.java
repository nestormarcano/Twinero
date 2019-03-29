package com.twinero.jtasks.nm.simplebanking.service;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.twinero.jtasks.nm.simplebanking.repository.beans.SignDAO;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.beans.BalanceResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.StatementResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.MovementResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.Session;
import com.twinero.jtasks.nm.simplebanking.service.beans.Movement;

/**
 * Service layer for the Simple Bank appliction.
 * @author Nestor Marcano
 */
@Service
@Validated
public interface SimpleBankService
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
	public SignDAO signup (@Valid SignDAO sign );

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------- login
	/**
	 * Performs a login in the system.
	 * 
	 * @param session The login data.
	 * 
	 * @return Object with the response data.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public Session login (@Valid Session session )
		throws SimpleBankServiceException;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------ getBalance
	/**
	 * Gets the account balance for a client.
	 * 
	 * @param clientID the client ID.
	 * @param sessionID The session ID.
	 * 
	 * @return The data of the response.
	 * @throws SimpleBankServiceException Object with the error data.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public BalanceResp getBalance (	long clientID,
												UUID sessionID )
		throws SimpleBankServiceException;

	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------- getStatement
	/**
	 * Gets the account statement for a client.
	 * 
	 * @param clientID the client ID.
	 * @param sessionID The session ID.
	 * 
	 * @return The data of the response.
	 * @throws SimpleBankServiceException Object with the error data.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public StatementResp getStatement (	long clientID,
													UUID sessionID )
		throws SimpleBankServiceException;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------- doDeposit
	/**
	 * Performs a deposit in the client's account.
	 * 
	 * @param deposit The deposit data.
	 * @param sessionID The session ID.
	 * 
	 * @return The data of the response.
	 * @throws SimpleBankServiceException Object with the error data.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public MovementResp doDeposit (	Movement deposit,
												UUID sessionID )
		throws SimpleBankServiceException;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------ doWithdraw
	/**
	 * Performs a withdraw in the client's account.
	 * 
	 * @param withdraw The withdraw data.
	 * @param sessionID The session ID.
	 * 
	 * @return The data of the response.
	 * @throws SimpleBankServiceException Object with the error data.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public MovementResp doWithdraw (	Movement withdraw,
												UUID sessionID )
		throws SimpleBankServiceException;
}
