package com.twinero.jtasks.nm.simplebanking.repository;

import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.beans.Withdraw;
import com.twinero.jtasks.nm.simplebanking.repository.beans.WithdrawResp;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;

@Repository
public interface WithdrawsRepository
{
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------- add
	/**
	 * Performs a withdraw in the client's account.
	 * 
	 * @param withdraw The withdraw request data.
	 * 
	 * @return The data of the response.
	 * @throws SimpleBankServiceException Object indicating a service error.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public WithdrawResp add (Withdraw withdraw )
		throws SimpleBankServiceException;
}
