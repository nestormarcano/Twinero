package com.twinero.jtasks.nm.simplebanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Java Developer coding task<br>
 * Coding challenge task is to create backend for a simple "banking" application:
 * <ul>
 * <li>Client should be able to sign up with email and password.</li>
 * <li>Client should be able to deposit money.</li>
 * <li>Client should be able to withdraw money.</li>
 * <li>Client should be able to see account balance and statement.</li>
 * </ul>
 * 
 * Note: Spring security usage is not necessary.
 * <p>
 * @author Nestor Marcano.
 */
@SpringBootApplication
public class SimpleBankingApplication
{
	public static void main (String[] args )
	{
		SpringApplication.run(SimpleBankingApplication.class, args);
	}
}
