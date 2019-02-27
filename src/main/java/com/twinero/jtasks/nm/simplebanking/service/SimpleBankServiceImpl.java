package com.twinero.jtasks.nm.simplebanking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twinero.jtasks.nm.simplebanking.beans.AccountBalance;
import com.twinero.jtasks.nm.simplebanking.beans.AccountBalanceResp;
import com.twinero.jtasks.nm.simplebanking.beans.AccountStatement;
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
import com.twinero.jtasks.nm.simplebanking.repository.beans.SesionStatus;
import com.twinero.jtasks.nm.simplebanking.utils.Util;

@Service
public class SimpleBankServiceImpl implements SimpleBankService
{
	@Autowired
	private SimpleBankRepository repository;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------- SimpleBankServiceImpl
	/**
	 * Constructor with the repository.
	 * @param repository The repository.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public SimpleBankServiceImpl ( SimpleBankRepository repository )
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
		if (sign.getEmail() == null || !Util.checksEmailFormat(sign.getEmail())
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
	public Session login (Sign sign )
		throws SimpleBankServiceException
	{
		if (sign.getEmail() == null || !Util.checksEmailFormat(sign.getEmail())
				|| sign.getPassword() == null || sign.getPassword().isEmpty())
		{ throw new SimpleBankServiceException(); }

		return repository.login(sign);
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
		SesionStatus estatus = repository.verifySession(sessionID);
		switch (estatus)
		{
			case OK:
				return repository.getAccountBalance(clientID);

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
		SesionStatus estatus = repository.verifySession(sessionID);
		switch (estatus)
		{
			case OK:
				return repository.getAccountStatement(clientID);

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
		SesionStatus estatus = repository.verifySession(sessionID);
		switch (estatus)
		{
			case OK:
				return repository.doDeposit(deposit);

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
		SesionStatus estatus = repository.verifySession(sessionID);
		switch (estatus)
		{
			case OK:
				return repository.doWithdraw(withdraw);

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
