package com.twinero.jtasks.nm.simplebanking.repository.impl;

import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.WithdrawsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Withdraw;
import com.twinero.jtasks.nm.simplebanking.repository.beans.WithdrawResp;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;

@Repository
public class SimpleBankRepositoryImpl implements WithdrawsRepository
{
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------- add
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public WithdrawResp add (Withdraw withdraw )
		throws SimpleBankServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
