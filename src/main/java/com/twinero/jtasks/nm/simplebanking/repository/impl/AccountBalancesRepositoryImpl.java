package com.twinero.jtasks.nm.simplebanking.repository.impl;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.AccountBalancesRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountBalance;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;

@Repository
public class AccountBalancesRepositoryImpl implements AccountBalancesRepository
{
	/*
	//@Autowired
   private SimpleBankingMapper mapper;

   public AccountBalancesRepositoryImpl(SimpleBankingMapper theMapper) {
       this.mapper = theMapper;
   }
   */
   
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------- get
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public AccountBalance get (long clientID )
		throws SimpleBankServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
