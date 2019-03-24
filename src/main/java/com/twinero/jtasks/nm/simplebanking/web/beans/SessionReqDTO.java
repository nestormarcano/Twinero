package com.twinero.jtasks.nm.simplebanking.web.beans;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.lang.NonNull;

import com.twinero.jtasks.nm.simplebanking.web.beans.annotations.Password;

public class SessionReqDTO
{
	public static final String emailRegExp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*"
			+ "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

	@Pattern(regexp = emailRegExp)
	private String email;

	@NonNull
	@NotBlank
	@Password
	private String password;

	public SessionReqDTO ()
	{
	}

	public SessionReqDTO (	String theEmail,
									String thePassword )
	{
		this.email = theEmail;
		this.password = thePassword;
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

		final SessionReqDTO other = (SessionReqDTO) obj;
		return (Objects.equals(this.email, other.email)
				&& Objects.equals(this.password, other.password));
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash(super.hashCode(), this.email, this.password);
	}

	/**
	 * @return the email
	 */
	public String getEmail ()
	{
		return this.email;
	}

	/**
	 * @param newEmail the email to set
	 */
	public void setEmail (String newEmail )
	{
		this.email = newEmail;
	}

	/**
	 * @return the password
	 */
	public String getPassword ()
	{
		return this.password;
	}

	/**
	 * @param newPassword the password to set
	 */
	public void setPassword (String newPassword )
	{
		this.password = newPassword;
	}
}
