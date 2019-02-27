package com.twinero.jtasks.nm.simplebanking.repository;

import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.beans.Deposit;
import com.twinero.jtasks.nm.simplebanking.repository.beans.DepositResp;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;

@Repository
public interface DepositsRepository
{
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------- add
	/**
	 * Performs a deposit in the client's account.
	 * 
	 * @param deposit The deposit request data.
	 * 
	 * @return The data of the response.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public DepositResp add (Deposit deposit )
		throws SimpleBankServiceException;
}
