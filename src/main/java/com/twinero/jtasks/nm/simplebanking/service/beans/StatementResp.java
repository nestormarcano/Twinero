package com.twinero.jtasks.nm.simplebanking.service.beans;

import java.util.Objects;

/**
 * Contains the response from the getStatements method of the service.<br>
 * The respond consists of a statement and a session status.
 * 
 * @author Nestor Marcano.
 */
// --------------------------------------------------------------------------------------------------------------------
public class StatementResp
{
	public static enum SessionStatus
	{
		UNDEFINED, OK, EXPIRED, DOES_NOT_EXIST, UNAUTHORIZED;
	}

	private SessionStatus sessionStatus;
	private Statement statement;

	public StatementResp ()
	{
	}

	public StatementResp (	Statement theStatement,
											SessionStatus theStatus )
	{
		this.statement = theStatement;
		this.sessionStatus = theStatus;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj )
	{
		if (this == obj)
			return true;

		if ((obj == null) || !this.getClass().equals(obj.getClass()))
			return false;

		final StatementResp other = (StatementResp) obj;
		return (Objects.equals(this.sessionStatus, other.sessionStatus)
				&& Objects.equals(this.statement, other.statement));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.sessionStatus, this.statement);
	}

	/**
	 * @return the sessionStatus.
	 */
	public SessionStatus getSessionStatus ()
	{
		return this.sessionStatus;
	}

	/**
	 * @param newSessionStatus the sessionStatus to set.
	 */
	public void setSessionStatus (SessionStatus newSessionStatus )
	{
		this.sessionStatus = newSessionStatus;
	}

	/**
	 * @return the statement.
	 */
	public Statement getStatement ()
	{
		return this.statement;
	}

	/**
	 * @param newStatement the statement to set.
	 */
	public void setStatement (Statement newStatement )
	{
		this.statement = newStatement;
	}
}
