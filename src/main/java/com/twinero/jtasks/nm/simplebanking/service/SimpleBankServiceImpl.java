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
import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountBalanceResp;
import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountStatement;
import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountStatementResp;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Deposit;
import com.twinero.jtasks.nm.simplebanking.repository.beans.DepositResp;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SesionStatus;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Session;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SignupResp;
import com.twinero.jtasks.nm.simplebanking.repository.beans.Withdraw;
import com.twinero.jtasks.nm.simplebanking.repository.beans.WithdrawResp;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
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
	public SignupResp signup (Sign sign )
		throws SimpleBankServiceException
	{
		if (sign.getEmail() == null || !Util.checksEmailFormat(sign.getEmail())
				|| sign.getPassword() == null || sign.getPassword().isEmpty())
		{ throw new SimpleBankServiceException(); }

		return signupsRepository.add(sign);
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
		if (sign.getEmail() == null || !Util.checksEmailFormat(sign.getEmail())
				|| sign.getPassword() == null || sign.getPassword().isEmpty())
		{ throw new SimpleBankServiceException(); }

		return sessionsRepository.add(sign);
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
		SesionStatus estatus = sessionsRepository.get(sessionID);
		switch (estatus)
		{
			case OK:
				return accountBalancesRepository.get(clientID);

			case EXPIRED:
				return new AccountBalanceResp(new AccountBalance(), AccountBalanceResp.Status.SESSION_EXPIRED);

			case NOT_EXISTS:
				return new AccountBalanceResp(new AccountBalance(), AccountBalanceResp.Status.SESSION_DOES_NOT_EXIST);

			case INTERNAL_ERROR:
				return new AccountBalanceResp(new AccountBalance(), AccountBalanceResp.Status.SERVER_ERROR);

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
		SesionStatus estatus = sessionsRepository.get(sessionID);
		switch (estatus)
		{
			case OK:
				return accountStatementsRepository.get(clientID);

			case EXPIRED:
				return new AccountStatementResp(new AccountStatement(), AccountStatementResp.Status.SESSION_EXPIRED);

			case NOT_EXISTS:
				return new AccountStatementResp(new AccountStatement(), AccountStatementResp.Status.SESSION_DOES_NOT_EXIST);

			case INTERNAL_ERROR:
				return new AccountStatementResp(new AccountStatement(), AccountStatementResp.Status.SERVER_ERROR);

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
		SesionStatus estatus = sessionsRepository.get(sessionID);
		switch (estatus)
		{
			case OK:
				return depositsRepository.add(deposit);

			case EXPIRED:
				return new DepositResp(new Deposit(), DepositResp.Status.SESSION_EXPIRED);

			case NOT_EXISTS:
				return new DepositResp(new Deposit(), DepositResp.Status.SESSION_DOES_NOT_EXIST);

			case INTERNAL_ERROR:
				return new DepositResp(new Deposit(), DepositResp.Status.SERVER_ERROR);

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
		SesionStatus estatus = sessionsRepository.get(sessionID);
		switch (estatus)
		{
			case OK:
				return withdrawsRepository.add(withdraw);

			case EXPIRED:
				return new WithdrawResp(new Withdraw(), WithdrawResp.Status.SESSION_EXPIRED);

			case NOT_EXISTS:
				return new WithdrawResp(new Withdraw(), WithdrawResp.Status.SESSION_DOES_NOT_EXIST);

			case INTERNAL_ERROR:
				return new WithdrawResp(new Withdraw(), WithdrawResp.Status.SERVER_ERROR);

			default:
				throw new SimpleBankServiceException();
		}
	}
}
