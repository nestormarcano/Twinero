package com.twinero.jtasks.nm.simplebanking.repository.impl;

import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.AccountStatementsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountStatementResp;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;

@Repository
public class AccountStatementsRepositoryImpl implements AccountStatementsRepository
{
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------- get
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public AccountStatementResp get (long clientID )
		throws SimpleBankServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
