package com.twinero.jtasks.nm.simplebanking.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.twinero.jtasks.nm.simplebanking.beans.AccountBalance;
import com.twinero.jtasks.nm.simplebanking.beans.AccountBalanceResp;
import com.twinero.jtasks.nm.simplebanking.beans.AccountStatement;
import com.twinero.jtasks.nm.simplebanking.beans.AccountStatementResp;
import com.twinero.jtasks.nm.simplebanking.beans.Deposit;
import com.twinero.jtasks.nm.simplebanking.beans.DepositReq;
import com.twinero.jtasks.nm.simplebanking.beans.DepositResp;
import com.twinero.jtasks.nm.simplebanking.beans.Session;
import com.twinero.jtasks.nm.simplebanking.beans.Sign;
import com.twinero.jtasks.nm.simplebanking.beans.SignupResp;
import com.twinero.jtasks.nm.simplebanking.beans.Withdraw;
import com.twinero.jtasks.nm.simplebanking.beans.WithdrawReq;
import com.twinero.jtasks.nm.simplebanking.beans.WithdrawResp;
import com.twinero.jtasks.nm.simplebanking.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.utils.Util;

@RestController
@RequestMapping("/simpleBanking")
public class SimpleBankingController
{
	private final SimpleBankService service;

	@Autowired
	public SimpleBankingController ( SimpleBankService service )
	{
		this.service = service;
	}

	// ---------------------------------------------------------------------------------------------------------- signup
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Performs a new sign up in the system.
	 * 
	 * @param sign A Sign object with the signup's data.
	 * @return An object with the new sign up just created.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@PostMapping(	path = "/signups", consumes = MediaType.APPLICATION_JSON_VALUE,
						produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> signup (@RequestBody(required = true) Sign sign )
	{
		try
		{
			if (!Util.checksEmailFormat(sign.getEmail()))
				return new ResponseEntity<String>(Util.asJsonString(new SignupResp(SignupResp.Status.BAD_REQUEST)),
						HttpStatus.BAD_REQUEST);

			SignupResp signup = service.signup(sign);

			if (signup.getStatus() == SignupResp.Status.ALREADY_EXISTS)
				return new ResponseEntity<String>(Util.asJsonString(signup), HttpStatus.CONFLICT);

			return new ResponseEntity<String>(Util.asJsonString(signup), HttpStatus.CREATED);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			SignupResp signup = new SignupResp(SignupResp.Status.SERVER_ERROR);
			return new ResponseEntity<String>(Util.asJsonString(signup), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ----------------------------------------------------------------------------------------------------------- login
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Performs a new login in the system.
	 * 
	 * @param sign A Sign object with the login's data.
	 * @return An object with the new sessionID just created.
	 */
	// ----------------------------------------------------------------------------------------------------------------
	@PostMapping(	path = "/sessions", consumes = MediaType.APPLICATION_JSON_VALUE,
						produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> login (@RequestBody(required = true) Sign sign )
	{
		try
		{
			if (!Util.checksEmailFormat(sign.getEmail()))
				return new ResponseEntity<String>(Util.asJsonString(new Session()), HttpStatus.BAD_REQUEST);

			Session session = service.login(sign);

			if (session.getSessionID() == null)
				return new ResponseEntity<String>(Util.asJsonString(session), HttpStatus.UNAUTHORIZED);

			return new ResponseEntity<String>(Util.asJsonString(session), HttpStatus.CREATED);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			Session session = new Session();
			return new ResponseEntity<String>(Util.asJsonString(session), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ----------------------------------------------------------------------------------------------- getAccountBalance
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Get the account balance for the client.
	 * 
	 * @param sessionID The sessionID returned by login.
	 * @return An object with the account balance.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@GetMapping(path = "accountBalances/{clientID}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String>
			getAccountBalance (	@PathVariable long clientID,
										@RequestHeader(name = "Authorization") String sessionID )
	{
		try
		{
			AccountBalanceResp response = service.getAccountBalance(clientID, sessionID);

			HttpStatus status;
			switch (response.getStatus())
			{
				case SESSION_EXPIRED:
				case SESSION_DOES_NOT_EXISTS:
					status = HttpStatus.UNAUTHORIZED;
					break;

				default:
					status = HttpStatus.OK;
					break;
			}

			return new ResponseEntity<String>(Util.asJsonString(response), status);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			AccountBalanceResp response = new AccountBalanceResp(new AccountBalance(),
					AccountBalanceResp.Status.SERVER_ERROR);
			return new ResponseEntity<String>(Util.asJsonString(response), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// --------------------------------------------------------------------------------------------- getAccountStatement
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Get the account statement for the client.
	 * 
	 * @param sessionID The sessionID returned by login.
	 * @return An object with the account balance.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@GetMapping(path = "accountStatements/{clientID}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String>
			getAccountStatement (@PathVariable long clientID,
										@RequestHeader(value = "Authorization") String sessionID )
	{
		try
		{
			AccountStatementResp response = service.getAccountStatement(clientID, sessionID);

			HttpStatus status = HttpStatus.OK;
			switch (response.getStatus())
			{
				case SESSION_EXPIRED:
				case SESSION_DOES_NOT_EXISTS:
					status = HttpStatus.UNAUTHORIZED;
					break;

				default:
					status = HttpStatus.OK;
					break;
			}

			return new ResponseEntity<String>(Util.asJsonString(response), status);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			AccountStatementResp response = new AccountStatementResp(new AccountStatement(),
					AccountStatementResp.Status.SERVER_ERROR);
			return new ResponseEntity<String>(Util.asJsonString(response), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ------------------------------------------------------------------------------------------------------- doDeposit
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Performs a deposit in a account of one client.
	 * 
	 * @param depositReq The request's data.
	 * @return An object with the new sessionID just created.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@PostMapping(	path = "/deposits", consumes = MediaType.APPLICATION_JSON_VALUE,
						produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> doDeposit (@RequestBody(required = true) DepositReq depositReq )
	{
		try
		{
			DepositResp depositResp = service.doDeposit(depositReq.getDeposit(), depositReq.getSessionID());

			HttpStatus status;
			switch (depositResp.getStatus())
			{
				case INVALID_CLIENT:
					status = HttpStatus.CONFLICT;
					break;

				case SESSION_EXPIRED:
				case SESSION_DOES_NOT_EXISTS:
					status = HttpStatus.UNAUTHORIZED;
					break;

				default:
					status = HttpStatus.CREATED;
					break;
			}

			return new ResponseEntity<String>(Util.asJsonString(depositResp), status);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			DepositResp depositResp = new DepositResp(new Deposit(), DepositResp.Status.SERVER_ERROR);
			return new ResponseEntity<String>(Util.asJsonString(depositResp), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ------------------------------------------------------------------------------------------------------ doWithdraw
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Performs a withdraw from an account of one client.
	 * 
	 * @param withdrawReq The request's data.
	 * @return An object with the new sessionID just created.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@PostMapping(	path = "/withdraws", consumes = MediaType.APPLICATION_JSON_VALUE,
						produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> doWithdraw (@RequestBody(required = true) WithdrawReq withdrawReq )
	{
		try
		{
			WithdrawResp withdrawResp = service.doWithdraw(withdrawReq.getWithdraw(), withdrawReq.getSessionID());

			HttpStatus status;
			switch (withdrawResp.getStatus())
			{
				case INVALID_CLIENT:
					status = HttpStatus.CONFLICT;
					break;

				case SESSION_EXPIRED:
				case SESSION_DOES_NOT_EXISTS:
					status = HttpStatus.UNAUTHORIZED;
					break;

				default:
					status = HttpStatus.CREATED;
					break;
			}

			return new ResponseEntity<String>(Util.asJsonString(withdrawResp), status);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			WithdrawResp withdrawResp = new WithdrawResp(new Withdraw(), WithdrawResp.Status.SERVER_ERROR);
			return new ResponseEntity<String>(Util.asJsonString(withdrawResp), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
