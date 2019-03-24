package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.math.BigDecimal;
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
import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.twinero.jtasks.nm.simplebanking.utils.MoneySerializer;

/**
 * DAO entity that contains the data for a client statement in the statements table.
 * @author Nestor Marcano.
 */
@Entity
@Table(name = "statements")
@EntityListeners(AuditingEntityListener.class)
public class StatementDAO
{
	@Id
	@Column(name = "statement_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NonNull
	@NotBlank
	@Column(nullable = false, updatable = true)
	private int year;

	@NonNull
	@NotBlank
	@Column(nullable = false, updatable = true)
	private int month;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal initialAmount;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal finalAmount;

	@NonNull
	@NotBlank
	@Column(columnDefinition = "VARCHAR(20) NOT NULL", nullable = false, updatable = true)
	private String accountNumber;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private SignDAO customer;

	public StatementDAO ()
	{
	}

	public StatementDAO ( long theId )
	{
		this.id = theId;
	}

	public StatementDAO (	long theCustomerID,
									int theYear,
									int theMonth )
	{
		this.year = theYear;
		this.month = theMonth;
		this.customer = new SignDAO(theCustomerID);
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

		final StatementDAO other = (StatementDAO) obj;
		return (Objects.equals(this.id, other.id)
				&& Objects.equals(this.year, other.year)
				&& Objects.equals(this.month, other.month)
				&& Objects.equals(this.initialAmount, other.initialAmount)
				&& Objects.equals(this.finalAmount, other.finalAmount)
				&& Objects.equals(this.accountNumber, other.accountNumber)
				&& Objects.equals(this.customer, other.customer));
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(this.id, this.year, this.month,
				this.initialAmount, this.finalAmount, this.accountNumber, this.customer);
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
	 * @return the year
	 */
	public int getYear ()
	{
		return year;
	}

	/**
	 * @param newYear the year to set
	 */
	public void setYear (int newYear )
	{
		this.year = newYear;
	}

	/**
	 * @return the month
	 */
	public int getMonth ()
	{
		return month;
	}

	/**
	 * @param newMonth the month to set
	 */
	public void setMonth (int newMonth )
	{
		this.month = newMonth;
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
	 * @return the initialAmount
	 */
	public BigDecimal getInitialAmount ()
	{
		return this.initialAmount;
	}

	/**
	 * @param newInitialAmount the initialAmount to set
	 */
	public void setInitialAmount (BigDecimal newInitialAmount )
	{
		this.initialAmount = newInitialAmount;
	}

	/**
	 * @return the finalAmount
	 */
	public BigDecimal getFinalAmount ()
	{
		return this.finalAmount;
	}

	/**
	 * @param newFinalAmount the finalAmount to set
	 */
	public void setFinalAmount (BigDecimal newFinalAmount )
	{
		this.finalAmount = newFinalAmount;
	}

	/**
	 * @return the customer
	 */
	public SignDAO getCustomer ()
	{
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer (SignDAO customer )
	{
		this.customer = customer;
	}
}
