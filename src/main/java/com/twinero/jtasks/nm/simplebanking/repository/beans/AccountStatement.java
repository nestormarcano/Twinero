package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AccountStatement extends ClientBaseObject
{
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSZ";
	public static final String TIMEZONE = "GMT-4";
	
	private long id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT, timezone = TIMEZONE)
	private Date since;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT, timezone = TIMEZONE)
	private Date  until;
	
	private String accountNumber;
	private List<Movement> movements;
	
	public AccountStatement()
	{
		
	}
	
	public AccountStatement(long theId)
	{
		this.id = theId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj )
	{
		if (super.equals(obj))
		{
			final AccountStatement other = (AccountStatement) obj;
			return (Objects.equals(this.id, other.id)
					&& Objects.equals(this.since, other.since)
					&& Objects.equals(this.until, other.until)
					&& Objects.equals(this.accountNumber, other.accountNumber)
					&& Objects.equals(this.movements, other.movements));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.id, this.since,
				this.until, this.accountNumber, this.movements);
	}

	/**
	 * @return the id
	 */
	public long getId ()
	{
		return this.id;
	}

	/**
	 * @param newId the id to set
	 */
	public void setId (long newId )
	{
		this.id = newId;
	}

	/**
	 * @return the since
	 */
	public Date getSince ()
	{
		return this.since;
	}

	/**
	 * @param newSince the since to set
	 */
	public void setSince (Date newSince )
	{
		this.since = newSince;
	}

	/**
	 * @return the until
	 */
	public Date getUntil ()
	{
		return this.until;
	}

	/**
	 * @param newUntil the until to set
	 */
	public void setUntil (Date newUntil )
	{
		this.until = newUntil;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber ()
	{
		return this.accountNumber;
	}

	/**
	 * @param newAccountNumber the accountNumber to set
	 */
	public void setAccountNumber (String newAccountNumber )
	{
		this.accountNumber = newAccountNumber;
	}

	/**
	 * @return the movements
	 */
	public List<Movement> getMovements ()
	{
		return this.movements;
	}

	/**
	 * @param newMovements the movements to set
	 */
	public void setMovements (List<Movement> newMovements )
	{
		this.movements = newMovements;
	}
}
