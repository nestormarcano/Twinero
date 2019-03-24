package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

import com.twinero.jtasks.nm.simplebanking.repository.beans.ClientBaseObject;

/**
 * Implements the DTO statement object.
 * @author Nestor Marcano.
 */
// --------------------------------------------------------------------------------------------------------------------
public class StatementDTO extends ClientBaseObject
{
	private long id;
	private String since;
	private String until;

	private String accountNumber;
	private Set<MovementDTO> movementsDTO;

	public StatementDTO ()
	{
	}

	public StatementDTO ( long theId )
	{
		this.id = theId;
	}

	public Date getSince (	SimpleDateFormat dateFormat,
									String timezone )
		throws ParseException
	{
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		return dateFormat.parse(this.since);
	}

	public void setSince (	Date newSince,
									SimpleDateFormat dateFormat,
									String timezone )
	{
		if (newSince == null)
		{
			this.since = null;
			return;
		}

		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		this.since = dateFormat.format(newSince);
	}

	public Date getUntil (	SimpleDateFormat dateFormat,
									String timezone )
		throws ParseException
	{
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		return dateFormat.parse(this.until);
	}

	public void setUntil (	Date newUntil,
									SimpleDateFormat dateFormat,
									String timezone )
	{
		if (newUntil == null)
		{
			this.until = null;
			return;
		}

		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		this.until = dateFormat.format(newUntil);
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
		
		if (super.equals(obj))
		{
			final StatementDTO other = (StatementDTO) obj;
			return (Objects.equals(this.id, other.id)
					&& Objects.equals(this.since, other.since)
					&& Objects.equals(this.until, other.until)
					&& Objects.equals(this.accountNumber, other.accountNumber)
					&& Objects.equals(this.movementsDTO, other.movementsDTO));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.id, this.since,
				this.until, this.accountNumber, this.movementsDTO);
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
	public String getSince ()
	{
		return this.since;
	}

	/**
	 * @param newSince the since to set
	 */
	public void setSince (String newSince )
	{
		this.since = newSince;
	}

	/**
	 * @return the until
	 */
	public String getUntil ()
	{
		return this.until;
	}

	/**
	 * @param newUntil the until to set
	 */
	public void setUntil (String newUntil )
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
	 * @return the movementsDTO
	 */
	public Set<MovementDTO> getMovementsDTO ()
	{
		return this.movementsDTO;
	}

	/**
	 * @param newMovementsDTO the movementsDTO to set
	 */
	public void setMovementsDTO (Set<MovementDTO> newMovementsDTO )
	{
		this.movementsDTO = newMovementsDTO;
	}
}
