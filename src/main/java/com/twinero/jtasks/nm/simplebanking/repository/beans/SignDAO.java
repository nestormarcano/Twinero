package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import com.twinero.jtasks.nm.simplebanking.repository.beans.annotations.UniqueEmail;
import com.twinero.jtasks.nm.simplebanking.web.beans.annotations.Password;

/**
 * DAO entity that contains the data for a client sign-up in the customers table.
 * @author Nestor Marcano.
 */
@Entity
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
public class SignDAO
{
	public static final String emailRegExp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*"
			+ "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

	@Id
	@Column(name = "customer_id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long signID;

	@NonNull
	@Pattern(regexp = emailRegExp)
	@UniqueEmail
	@Column(columnDefinition = "VARCHAR(255) NOT NULL", nullable = false, updatable = true, unique = true)
	private String email;

	@NonNull
	@NotBlank
	@Password
	@Column(columnDefinition = "VARCHAR(255) NOT NULL", nullable = false, updatable = true)
	private String password;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(	columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL",
				nullable = true, insertable = false, updatable = false)
	private Date createdAt;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = true, insertable = false, updatable = true)
	private Date updatedAt;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<SessionDAO> sessions;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<MovementDAO> movements;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<StatementDAO> statements;

	/**
	 * Default constructor.
	 */
	public SignDAO ()
	{
	}

	/**
	 * Constructor with signID.
	 * 
	 * @param theSignID    The sign id.
	 */
	public SignDAO ( Long theSignID )
	{
		this.signID = theSignID;
	}

	/**
	 * Constructor with email and password.
	 * 
	 * @param email    Customer's email.
	 * @param password Customr's password.
	 */
	public SignDAO (	String email,
							String password )
	{
		this.email = email;
		this.password = password;
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

		final SignDAO other = (SignDAO) obj;
		return (Objects.equals(this.signID, other.signID) && Objects.equals(this.email, other.email)
				&& Objects.equals(this.password, other.password));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode ()
	{
		return Objects.hash(this.signID, this.email, this.password);
	}

	/**
	 * @return the signID
	 */
	public Long getSignID ()
	{
		return this.signID;
	}

	/**
	 * @param newSignID the signID to set
	 */
	public void setSignID (Long newSignID )
	{
		this.signID = newSignID;
	}

	/**
	 * @return the email
	 */
	public String getEmail ()
	{
		return this.email;
	}

	/**
	 * @param newEmail the email to set.
	 */
	public void setEmail (String newEmail )
	{
		this.email = newEmail;
	}

	/**
	 * @return the password.
	 */
	public String getPassword ()
	{
		return this.password;
	}

	/**
	 * @param newPassword the password to set.
	 */
	public void setPassword (String newPassword )
	{
		this.password = newPassword;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt ()
	{
		return this.createdAt;
	}

	/**
	 * @param newCreatedAt the createdAt to set
	 */
	public void setCreatedAt (Date newCreatedAt )
	{
		this.createdAt = newCreatedAt;
	}

	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt ()
	{
		return this.updatedAt;
	}

	/**
	 * @param newUpdatedAt the updatedAt to set
	 */
	public void setUpdatedAt (Date newUpdatedAt )
	{
		this.updatedAt = newUpdatedAt;
	}

	/**
	 * @return the sessions
	 */
	public Set<SessionDAO> getSessions ()
	{
		return this.sessions;
	}

	/**
	 * @param newSessions the sessions to set
	 */
	public void setSessions (Set<SessionDAO> newSessions )
	{
		this.sessions = newSessions;
	}

	/**
	 * @return the movements
	 */
	public Set<MovementDAO> getMovements ()
	{
		return this.movements;
	}

	/**
	 * @param newMovements the movements to set
	 */
	public void setMovements (Set<MovementDAO> newMovements )
	{
		this.movements = newMovements;
	}

	/**
	 * @return the statements
	 */
	public Set<StatementDAO> getStatements ()
	{
		return this.statements;
	}

	/**
	 * @param newStatements the statements to set
	 */
	public void setStatements (Set<StatementDAO> newStatements )
	{
		this.statements = newStatements;
	}
}
