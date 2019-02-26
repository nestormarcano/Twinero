package com.twinero.jtasks.nm.simplebanking.repository;

import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.beans.AccountBalanceResp;
import com.twinero.jtasks.nm.simplebanking.beans.AccountStatementResp;
import com.twinero.jtasks.nm.simplebanking.beans.Session;
import com.twinero.jtasks.nm.simplebanking.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.beans.SignupResp;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;

@Repository
public class SimpleBankRepositoryImpl implements SimpleBankRepository
{
	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------- signup
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public SignupResp signup (Sign sign )
		throws SimpleBankServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------- login
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public Session login (Sign sign )
		throws SimpleBankServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------- getAccountBalance
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public AccountBalanceResp getAccountBalance (long clientID,
																String sessionID )
		throws SimpleBankServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------- getAccountStatement
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public AccountStatementResp getAccountStatement (	long clientID,
																		String sessionID )
		throws SimpleBankServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
