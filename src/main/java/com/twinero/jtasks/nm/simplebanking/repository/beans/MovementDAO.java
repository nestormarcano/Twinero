package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

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
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twinero.jtasks.nm.simplebanking.utils.MoneySerializer;

@Entity
@Table(name = "movements")
@EntityListeners(AuditingEntityListener.class)
public class MovementDAO extends ClientBaseObject
{
	public static enum Type
	{
		DEPOSIT, WITHDRAW;
	}

	@Id
	@Column(name = "movement_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long movementID;

	@NonNull
	@NotBlank
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal amount;
	
	@NonNull
	@NotBlank
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal tax;

	@NonNull
	@NotBlank
	@Column(nullable = false, updatable = true)
	private Type type;

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date time;

	@NonNull
	@NotBlank
	@Column(columnDefinition = "VARCHAR(255) NOT NULL", nullable = false, updatable = true)
	private String reference;
	
	@Column(columnDefinition = "VARCHAR(255) NULL", nullable = true, updatable = true)
	private String description;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private SignDAO customer;

	public MovementDAO ()
	{
	}

	public MovementDAO ( long theMovementID )
	{
		this.movementID = theMovementID;
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
			final MovementDAO other = (MovementDAO) obj;
			return (Objects.equals(this.movementID, other.movementID)
					&& Objects.equals(this.amount, other.amount)
					&& Objects.equals(this.tax, other.tax)
					&& Objects.equals(this.type, other.type)
					&& Objects.equals(this.time, other.time)
					&& Objects.equals(this.reference, other.reference)
					&& Objects.equals(this.description, other.description)
					&& Objects.equals(this.customer, other.customer));
		}

		return false;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.movementID,
				this.amount, this.tax, this.type, this.time, this.reference, this.description, this.customer);
	}

	/**
	 * @return the movementID
	 */
	public long getMovementID ()
	{
		return this.movementID;
	}

	/**
	 * @param newMovementID the movementID to set
	 */
	public void setMovementID (long newMovementID )
	{
		this.movementID = newMovementID;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount ()
	{
		return this.amount;
	}

	/**
	 * @param newAmount the amount to set
	 */
	public void setAmount (BigDecimal newAmount )
	{
		this.amount = newAmount;
	}
	
	/**
	 * @return the tax
	 */
	public BigDecimal getTax ()
	{
		return tax;
	}

	/**
	 * @param newTax the tax to set
	 */
	public void setTax (BigDecimal newTax )
	{
		this.tax = newTax;
	}

	/**
	 * @return the type
	 */
	public Type getType ()
	{
		return type;
	}

	/**
	 * @param newType the type to set
	 */
	public void setType (Type newType )
	{
		this.type = newType;
	}

	/**
	 * @return the time
	 */
	public Date getTime ()
	{
		return this.time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime (Date newTime )
	{
		this.time = newTime;
	}

	/**
	 * @return the reference
	 */
	public String getReference ()
	{
		return this.reference;
	}

	/**
	 * @param newReference the reference to set
	 */
	public void setReference (String newReference )
	{
		this.reference = newReference;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription ()
	{
		return description;
	}

	/**
	 * @param newDescription the description to set
	 */
	public void setDescription (String newDescription )
	{
		this.description = newDescription;
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
