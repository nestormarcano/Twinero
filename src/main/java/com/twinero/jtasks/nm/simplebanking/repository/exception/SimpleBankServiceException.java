package com.twinero.jtasks.nm.simplebanking.repository.exception;

/**
 * The exception to be throws when an error occurs.
 * @author Nestor Marcano.
 */
public class SimpleBankServiceException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor.
	 */
	public SimpleBankServiceException()
	{
		super();
	}
	
	/**
	 * Constructor with a cause.
	 * @param cause Cause of the parent exception.
	 */
	public SimpleBankServiceException(Throwable cause)
	{
		super(cause);
	}
}
