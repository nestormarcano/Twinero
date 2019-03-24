package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.util.Objects;

public class StatementRespDTO
{
	public static enum SessionStatus
	{
		UNDEFINED, OK, EXPIRED, DOES_NOT_EXIST, UNAUTHORIZED;
	}

	private SessionStatus sessionStatus;
	private StatementDTO statement;

	public StatementRespDTO ()
	{
	}

	public StatementRespDTO (	StatementDTO theStatement,
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

		final StatementRespDTO other = (StatementRespDTO) obj;
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
	public StatementDTO getStatement ()
	{
		return this.statement;
	}

	/**
	 * @param newStatement the statement to set.
	 */
	public void setStatement (StatementDTO newStatement )
	{
		this.statement = newStatement;
	}
}
