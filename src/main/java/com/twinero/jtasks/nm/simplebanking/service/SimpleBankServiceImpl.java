package com.twinero.jtasks.nm.simplebanking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twinero.jtasks.nm.simplebanking.beans.AccountBalanceResp;
import com.twinero.jtasks.nm.simplebanking.beans.AccountStatementResp;
import com.twinero.jtasks.nm.simplebanking.beans.Deposit;
import com.twinero.jtasks.nm.simplebanking.beans.DepositResp;
import com.twinero.jtasks.nm.simplebanking.beans.Session;
import com.twinero.jtasks.nm.simplebanking.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.beans.SignupResp;
import com.twinero.jtasks.nm.simplebanking.beans.Withdraw;
import com.twinero.jtasks.nm.simplebanking.beans.WithdrawResp;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.repository.SimpleBankRepository;

@Service
public class SimpleBankServiceImpl implements SimpleBankService
{
	@Autowired
	private SimpleBankRepository repository;
	
	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------- SimpleBankServiceImpl
	/**
	 * Constructor with the repository.
	 * @param theRepository The repository.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public SimpleBankServiceImpl(SimpleBankRepository repository)
	{
		this.repository = repository;
	}
	
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
		if (sign.getEmail() == null || sign.getEmail().isEmpty()
				|| sign.getPassword() == null || sign.getPassword().isEmpty())
		{ throw new SimpleBankServiceException(); }
		
		return repository.signup(sign);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------- login
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public Session login (Sign newSign )
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

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------- doDeposit
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public DepositResp doDeposit (Deposit deposit,
											String sessionID )
		throws SimpleBankServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------ doWithdraw
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public WithdrawResp doWithdraw (	Withdraw depositForReq,
												String sessionID )
		throws SimpleBankServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
