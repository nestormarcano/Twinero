package com.twinero.jtasks.nm.simplebanking.service;

import org.springframework.stereotype.Service;

import com.twinero.jtasks.nm.simplebanking.beans.AccountBalanceResp;
import com.twinero.jtasks.nm.simplebanking.beans.AccountStatementResp;
import com.twinero.jtasks.nm.simplebanking.beans.Deposit;
import com.twinero.jtasks.nm.simplebanking.beans.DepositResp;
import com.twinero.jtasks.nm.simplebanking.beans.Session;
import com.twinero.jtasks.nm.simplebanking.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.beans.SignupResp;
import com.twinero.jtasks.nm.simplebanking.beans.Withdraw;
import com.twinero.jtasks.nm.simplebanking.beans.WithdrawResp;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;

@Service
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
	 * Gets the account balance for a client.
	 * 
	 * @param clientID the client ID.
	 * @param sessionID The session ID.
	 * 
	 * @return The data of the response.
	 * @throws SimpleBankServiceException Object with the error data.
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
	 * @param clientID the client ID.
	 * @param sessionID The session ID.
	 * 
	 * @return The data of the response.
	 * @throws SimpleBankServiceException Object with the error data.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public AccountStatementResp getAccountStatement (	long clientID,
																		String sessionID )
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
	public DepositResp doDeposit (Deposit deposit,
											String sessionID )
		throws SimpleBankServiceException;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------ doWithdraw
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public WithdrawResp doWithdraw (	Withdraw depositForReq,
												String sessionID )
		throws SimpleBankServiceException;
}
