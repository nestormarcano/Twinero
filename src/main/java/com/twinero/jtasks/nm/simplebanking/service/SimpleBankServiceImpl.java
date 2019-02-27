package com.twinero.jtasks.nm.simplebanking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twinero.jtasks.nm.simplebanking.repository.AccountBalancesRepository;
import com.twinero.jtasks.nm.simplebanking.repository.AccountStatementsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.DepositsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.SessionsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.SignupsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.WithdrawsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountBalance;
import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountStatement;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Deposit;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Withdraw;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.beans.AccountBalanceResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.AccountStatementResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.DepositResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.SignReq;
import com.twinero.jtasks.nm.simplebanking.service.beans.SignupResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.WithdrawResp;
import com.twinero.jtasks.nm.simplebanking.utils.Util;

@Service
public class SimpleBankServiceImpl implements SimpleBankService
{	
	@Autowired
	private SignupsRepository signupsRepository;
	
	@Autowired
	private SessionsRepository sessionsRepository;
	
	@Autowired
	private AccountBalancesRepository accountBalancesRepository;
	
	@Autowired
	private AccountStatementsRepository accountStatementsRepository;
	
	@Autowired
	private DepositsRepository depositsRepository;
	
	@Autowired
	private WithdrawsRepository withdrawsRepository;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------- SimpleBankServiceImpl
	/**
	 * Constructor with the repository.
	 * 
	 * @param theSignupsRepository The sign-ups repository.
	 * @param theSessionsRepository The sessions repository.
	 * @param theAccountBalancesRepository The account balances repository.
	 * @param theAccountStatementsRepository The statements repository.
	 * @param theDepositsRepository The Deposits repository.
	 * @param theWithdrawsRepository The Withdraws repository.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public SimpleBankServiceImpl ( SignupsRepository theSignupsRepository,
	                               SessionsRepository theSessionsRepository,
	                               AccountBalancesRepository theAccountBalancesRepository,
	                               AccountStatementsRepository theAccountStatementsRepository,
	                               DepositsRepository theDepositsRepository,
	                               WithdrawsRepository theWithdrawsRepository)
	{
		this.signupsRepository = theSignupsRepository;
		this.sessionsRepository = theSessionsRepository;
		this.accountBalancesRepository = theAccountBalancesRepository;
		this.accountStatementsRepository = theAccountStatementsRepository;
		this.depositsRepository = theDepositsRepository;
		this.withdrawsRepository = theWithdrawsRepository;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------- signup
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public SignupResp signup (SignReq signReq )
		throws SimpleBankServiceException
	{
		if (signReq.getEmail() == null || !Util.checksEmailFormat(signReq.getEmail())
				|| signReq.getPassword() == null || signReq.getPassword().isEmpty())
		{ throw new SimpleBankServiceException(); }

		Sign sign = new Sign(signReq.getEmail(), signReq.getPassword());
		
		sign = signupsRepository.add(sign);
		
		if (sign.getSignID() == 0)
			return new SignupResp(SignupResp.Status.ALREADY_EXISTS);
		
		return new SignupResp(SignupResp.Status.OK);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------- login
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public Session login (Session session )
		throws SimpleBankServiceException
	{
		if (session.getEmail() == null || !Util.checksEmailFormat(session.getEmail())
				|| session.getPassword() == null || session.getPassword().isEmpty())
		{ throw new SimpleBankServiceException(); }

		return sessionsRepository.add(session);
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
		Session estatus = sessionsRepository.get(sessionID);
		switch (estatus.getSessionStatus())
		{
			case OK:
				AccountBalance account = accountBalancesRepository.get(clientID);
				return new AccountBalanceResp(account, AccountBalanceResp.Status.OK);

			case EXPIRED:
				return new AccountBalanceResp(new AccountBalance(), AccountBalanceResp.Status.SESSION_EXPIRED);

			case NOT_EXISTS:
				return new AccountBalanceResp(new AccountBalance(), AccountBalanceResp.Status.SESSION_DOES_NOT_EXIST);

			case INTERNAL_ERROR:
				return new AccountBalanceResp(new AccountBalance(), AccountBalanceResp.Status.SERVER_ERROR);
				
			case UNAUTHORIZED:
				return new AccountBalanceResp(new AccountBalance(), AccountBalanceResp.Status.UNAUTHORIZED);

			default:
				throw new SimpleBankServiceException();
		}
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
		Session estatus = sessionsRepository.get(sessionID);
		switch (estatus.getSessionStatus())
		{
			case OK:
				AccountStatement account = accountStatementsRepository.get(clientID);
				return new AccountStatementResp(account, AccountStatementResp.Status.OK);

			case EXPIRED:
				return new AccountStatementResp(new AccountStatement(), AccountStatementResp.Status.SESSION_EXPIRED);

			case NOT_EXISTS:
				return new AccountStatementResp(new AccountStatement(), AccountStatementResp.Status.SESSION_DOES_NOT_EXIST);

			case INTERNAL_ERROR:
				return new AccountStatementResp(new AccountStatement(), AccountStatementResp.Status.SERVER_ERROR);
				
			case UNAUTHORIZED:
				return new AccountStatementResp(new AccountStatement(), AccountStatementResp.Status.UNAUTHORIZED);

			default:
				throw new SimpleBankServiceException();
		}
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
		Session estatus = sessionsRepository.get(sessionID);
		switch (estatus.getSessionStatus())
		{
			case OK:
				deposit = depositsRepository.add(deposit);
				if (deposit.getClientID() == 0)
					return new DepositResp(deposit, DepositResp.Status.INVALID_CLIENT);
				
				return new DepositResp(deposit, DepositResp.Status.OK);

			case EXPIRED:
				return new DepositResp(new Deposit(), DepositResp.Status.SESSION_EXPIRED);

			case NOT_EXISTS:
				return new DepositResp(new Deposit(), DepositResp.Status.SESSION_DOES_NOT_EXIST);

			case INTERNAL_ERROR:
				return new DepositResp(new Deposit(), DepositResp.Status.SERVER_ERROR);
				
			case UNAUTHORIZED:
				return new DepositResp(new Deposit(), DepositResp.Status.UNAUTHORIZED);

			default:
				throw new SimpleBankServiceException();
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------ doWithdraw
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public WithdrawResp doWithdraw (	Withdraw withdraw,
												String sessionID )
		throws SimpleBankServiceException
	{
		Session estatus = sessionsRepository.get(sessionID);
		switch (estatus.getSessionStatus())
		{
			case OK:
				withdraw = withdrawsRepository.add(withdraw);
				if (withdraw.getClientID() == 0)
					return new WithdrawResp(withdraw, WithdrawResp.Status.INVALID_CLIENT);
				
				return new WithdrawResp(withdraw, WithdrawResp.Status.OK);

			case EXPIRED:
				return new WithdrawResp(new Withdraw(), WithdrawResp.Status.SESSION_EXPIRED);

			case NOT_EXISTS:
				return new WithdrawResp(new Withdraw(), WithdrawResp.Status.SESSION_DOES_NOT_EXIST);

			case INTERNAL_ERROR:
				return new WithdrawResp(new Withdraw(), WithdrawResp.Status.SERVER_ERROR);
				
			case UNAUTHORIZED:
				return new WithdrawResp(new Withdraw(), WithdrawResp.Status.UNAUTHORIZED);

			default:
				throw new SimpleBankServiceException();
		}
	}
}
