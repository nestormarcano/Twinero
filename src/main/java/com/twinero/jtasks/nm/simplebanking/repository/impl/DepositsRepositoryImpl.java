package com.twinero.jtasks.nm.simplebanking.repository.impl;

import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.DepositsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Deposit;
import com.twinero.jtasks.nm.simplebanking.repository.beans.DepositResp;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;

@Repository
public class DepositsRepositoryImpl implements DepositsRepository
{
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------- doDeposit
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public DepositResp add (Deposit deposit )
		throws SimpleBankServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
