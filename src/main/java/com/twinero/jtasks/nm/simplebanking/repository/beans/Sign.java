package com.twinero.jtasks.nm.simplebanking.repository.beans;

import java.util.Objects;

/**
 * Contains the signing data.<br>
 * Usually it is the data to sign-up.
 * 
 * @author Nestor Marcano
 */
public class Sign
{
	private String email;
	private String password;

	/**
	 * Default constructor.
	 */
	public Sign ()
	{
	}

	/**
	 * Constructor with email and password.
	 * 
	 * @param email    Customer's email.
	 * @param password Customr's password.
	 */
	public Sign (	String email,
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
		if (super.equals(obj))
			return true;

		if ((obj == null) || !this.getClass().equals(obj.getClass()))
		{ return false; }

		final Sign other = (Sign) obj;
		return (Objects.equals(this.email, other.email) && Objects.equals(this.password, other.password));
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(this.email, this.password);
	}

	/**
	 * @return the email
	 */
	public String getEmail ()
	{
		return email;
	}

	/**
	 * @param email the email to set.
	 */
	public void setEmail (String email )
	{
		this.email = email;
	}

	/**
	 * @return the password.
	 */
	public String getPassword ()
	{
		return password;
	}

	/**
	 * @param password the password to set.
	 */
	public void setPassword (String password )
	{
		this.password = password;
	}
}
