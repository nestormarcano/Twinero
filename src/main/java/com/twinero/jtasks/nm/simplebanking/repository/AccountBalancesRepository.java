package com.twinero.jtasks.nm.simplebanking.repository;

import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountBalance;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;

@Repository
public interface AccountBalancesRepository //extends IRepository<AccountBalanceResp, Long>
{
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------- get
	/**
	 * Gets the account balances for a client.
	 * 
	 * @param clientID The identification of the client.
	 * 
	 * @return The account balance.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public AccountBalance get (long clientID )
		throws SimpleBankServiceException;
}
