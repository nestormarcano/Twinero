package com.twinero.jtasks.nm.simplebanking.repository.impl;

import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.SessionsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SesionStatus;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;

@Repository
public class SessionsRepositoryImpl implements SessionsRepository
{
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------- get
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public SesionStatus get (String sessionID )
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
	public Session add (Sign sign )
		throws SimpleBankServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
