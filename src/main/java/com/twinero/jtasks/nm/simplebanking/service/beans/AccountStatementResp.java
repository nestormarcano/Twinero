package com.twinero.jtasks.nm.simplebanking.service.beans;

import java.util.Objects;

import com.twinero.jtasks.nm.simplebanking.repository.beans.AccountStatement;

public class AccountStatementResp
{
	public static enum Status
	{
		OK, SESSION_EXPIRED, SESSION_DOES_NOT_EXIST, SERVER_ERROR, UNAUTHORIZED;
	}

	private Status status;
	private AccountStatement statement;

	public AccountStatementResp ()
	{
	}

	public AccountStatementResp (	AccountStatement theStatement,
											Status theStatus )
	{
		this.statement = theStatement;
		this.status = theStatus;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj )
	{
		if (super.equals(obj))
			return true;

		if ((obj == null) || !this.getClass().equals(obj.getClass()))
		{ return false; }

		final AccountStatementResp other = (AccountStatementResp) obj;
		return (Objects.equals(this.status, other.status) && Objects.equals(this.statement, other.statement));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.status, this.statement);
	}

	/**
	 * @return the status.
	 */
	public Status getStatus ()
	{
		return this.status;
	}

	/**
	 * @param newStatus the status to set.
	 */
	public void setStatus (Status newStatus )
	{
		this.status = newStatus;
	}

	/**
	 * @return the statement.
	 */
	public AccountStatement getStatement ()
	{
		return this.statement;
	}

	/**
	 * @param newStatement the statement to set.
	 */
	public void setStatement (AccountStatement newStatement )
	{
		this.statement = newStatement;
	}
}
