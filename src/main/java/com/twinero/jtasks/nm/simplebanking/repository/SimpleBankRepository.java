package com.twinero.jtasks.nm.simplebanking.repository;

import org.springframework.stereotype.Repository;

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
import com.twinero.jtasks.nm.simplebanking.repository.beans.SesionStatus;

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
	 * 
	 * @return The account balance.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public AccountBalanceResp getAccountBalance (long clientID )
		throws SimpleBankServiceException;

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------- getAccountStatement
	/**
	 * Gets the account statement for a client.
	 * 
	 * @param clientID The identification of the client.
	 * 
	 * @return The account statement.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public AccountStatementResp getAccountStatement (long clientID )
		throws SimpleBankServiceException;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------- doDeposit
	/**
	 * Performs a deposit in the client's account.
	 * 
	 * @param deposit The deposit request data.
	 * 
	 * @return The data of the response.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public DepositResp doDeposit (Deposit deposit )
		throws SimpleBankServiceException;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------ doWithdraw
	/**
	 * Performs a withdraw in the client's account.
	 * 
	 * @param withdraw The withdraw request data.
	 * 
	 * @return The data of the response.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public WithdrawResp doWithdraw (Withdraw withdraw )
		throws SimpleBankServiceException;

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------- verifySession
	/**
	 * Performs a session verification.
	 * 
	 * @param sessionID The identification of the session.
	 * 
	 * @return The status of the session.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public SesionStatus verifySession (String sessionID )
		throws SimpleBankServiceException;
}
