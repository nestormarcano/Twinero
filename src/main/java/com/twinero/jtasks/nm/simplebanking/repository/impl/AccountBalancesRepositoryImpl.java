package com.twinero.jtasks.nm.simplebanking.repository.impl;

import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.AccountBalancesRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountBalanceResp;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;

@Repository
public class AccountBalancesRepositoryImpl implements AccountBalancesRepository
{
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------- get
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public AccountBalanceResp get (long clientID )
		throws SimpleBankServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
