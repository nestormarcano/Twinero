package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.lang.NonNull;

import com.twinero.jtasks.nm.simplebanking.web.beans.annotations.Password;

public class SignReqDTO
{
	public static final String emailRegExp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*"
			+ "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

	@Pattern(regexp = emailRegExp)
	private String email;

	@NonNull
	@NotBlank
	@Password
	private String password;

	/**
	 * Default constructor.
	 */
	public SignReqDTO ()
	{
	}

	/**
	 * Constructor with email and password.
	 * 
	 * @param email    Customer's email.
	 * @param password Customr's password.
	 */
	public SignReqDTO (	String email,
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

		final SignReqDTO other = (SignReqDTO) obj;
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
