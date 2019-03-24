package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "sessions")
@EntityListeners(AuditingEntityListener.class)
public class SessionDAO
{
	@Id
	@Column(name = "session_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID sessionID;

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date time;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private SignDAO customer;

	public SessionDAO ()
	{
	}

	public SessionDAO ( UUID theSessionID )
	{
		this.sessionID = theSessionID;
	}

	public SessionDAO ( SignDAO theCustomer )
	{
		this.customer = theCustomer;
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

		final SessionDAO other = (SessionDAO) obj;
		return (Objects.equals(this.sessionID, other.sessionID)
				&& Objects.equals(this.customer, other.customer)
				&& Objects.equals(this.time, other.time));
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.sessionID, this.customer, this.time);
	}

	/**
	 * @return the sessionID
	 */
	public UUID getSessionID ()
	{
		return this.sessionID;
	}

	/**
	 * @param newSessionID the sessionID to set.
	 */
	public void setSessionID (UUID newSessionID )
	{
		this.sessionID = newSessionID;
	}

	/**
	 * @return the time
	 */
	public Date getTime ()
	{
		return this.time;
	}

	/**
	 * @param newTime the time to set
	 */
	public void setTime (Date newTime )
	{
		this.time = newTime;
	}

	/**
	 * @return the customer
	 */
	public SignDAO getCustomer ()
	{
		return this.customer;
	}

	/**
	 * @param newCustomer the customer to set
	 */
	public void setCustomer (SignDAO newCustomer )
	{
		this.customer = newCustomer;
	}
}
