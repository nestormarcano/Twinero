package com.twinero.jtasks.nm.simplebanking.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.UUID;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

import com.twinero.jtasks.nm.simplebanking.repository.beans.SignDAO;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.SimpleBankService;
import com.twinero.jtasks.nm.simplebanking.service.beans.BalanceResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.StatementResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.MovementResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.Session;
import com.twinero.jtasks.nm.simplebanking.service.beans.Movement;
import com.twinero.jtasks.nm.simplebanking.utils.Util;
import com.twinero.jtasks.nm.simplebanking.web.beans.BalanceRespDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.StatementRespDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.DepositDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.DepositReqDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.DepositRespDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.MovementDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.SessionReqDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.SessionRespDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.SignReqDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.SignRespDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.WithdrawDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.WithdrawReqDTO;
import com.twinero.jtasks.nm.simplebanking.web.beans.WithdrawRespDTO;

@RestController
@RequestMapping("/simpleBanking")
public class SimpleBankingController
{
	private static final String TIME_ZONE = "GMT-4";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	private final SimpleBankService service;

	@Bean
	public ModelMapper modelMapper ()
	{
		return new ModelMapper();
	}

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	public SimpleBankingController ( SimpleBankService service )
	{
		this.service = service;
	}

	// --------------------------------------------------------------------------------------------------------- sign-up
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Performs a new sign up in the system.
	 * 
	 * @param signReqDTO A SignDAO object with the signup's data.
	 * @return An object with the new sign up just created.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@PostMapping(	path = "/signups", consumes = MediaType.APPLICATION_JSON_VALUE,
						produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> signup (@Valid @RequestBody(required = true) SignReqDTO signReqDTO )
	{
		try
		{
			SignDAO sign = modelMapper.map(signReqDTO, SignDAO.class);
			sign = service.signup(sign);

			SignRespDTO signRespDTO = modelMapper.map(sign, SignRespDTO.class);
			HttpStatus httpStatus;

			if (signRespDTO.getSignID() == 0L)
			{
				signRespDTO.setStatus(SignRespDTO.Status.ALREADY_EXISTS);
				httpStatus = HttpStatus.CONFLICT;
			}
			else if (signRespDTO.getSignID() > 0L)
			{
				signRespDTO.setStatus(SignRespDTO.Status.OK);
				httpStatus = HttpStatus.CREATED;
			}
			else throw new SimpleBankServiceException();

			return new ResponseEntity<String>(Util.asJsonString(signRespDTO), httpStatus);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			SignRespDTO sign = new SignRespDTO(SignRespDTO.Status.SERVER_ERROR);
			sign.setEmail(signReqDTO.getEmail());
			return new ResponseEntity<String>(Util.asJsonString(sign), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ----------------------------------------------------------------------------------------------------------- login
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Performs a new login in the system.
	 * 
	 * @param sessionReqDTO A session request object with the login's data.
	 * @return An object with the new sessionID just created.
	 */
	// ----------------------------------------------------------------------------------------------------------------
	@PostMapping(	path = "/sessions", consumes = MediaType.APPLICATION_JSON_VALUE,
						produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String>
			login (@Valid @RequestBody(required = true) SessionReqDTO sessionReqDTO )
	{
		try
		{
			Session session = modelMapper.map(sessionReqDTO, Session.class);
			session = service.login(session);

			SessionRespDTO sessionRespDTO = modelMapper.map(session, SessionRespDTO.class);

			HttpStatus httpStatus = HttpStatus.CREATED;
			if (session.getSessionID() == null)
				httpStatus = HttpStatus.UNAUTHORIZED;

			return new ResponseEntity<String>(Util.asJsonString(sessionRespDTO), httpStatus);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			SessionRespDTO sessionRespDTO = new SessionRespDTO();
			return new ResponseEntity<String>(Util.asJsonString(sessionRespDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ----------------------------------------------------------------------------------------------- getAccountBalance
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Get the account balance for the client.
	 * 
	 * @param clientID The client's ID.
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
			UUID sessionUUID = UUID.fromString(sessionID);
			BalanceResp response = service.getBalance(clientID, sessionUUID);
			BalanceRespDTO responseDTO = convertToAccountBalanceRespDTO(response);

			HttpStatus httpStatus;
			switch (responseDTO.getSessionStatus())
			{
				case EXPIRED:
				case DOES_NOT_EXIST:
					httpStatus = HttpStatus.UNAUTHORIZED;
					break;

				default:
					httpStatus = HttpStatus.OK;
					break;
			}

			return new ResponseEntity<String>(Util.asJsonString(responseDTO), httpStatus);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			BalanceRespDTO responseDTO = new BalanceRespDTO();
			return new ResponseEntity<String>(Util.asJsonString(responseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// --------------------------------------------------------------------------------------------- getAccountStatement
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Get the account statement for the client.
	 * 
	 * @param clientID The client's ID.
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
			UUID sessionUUID = UUID.fromString(sessionID);
			StatementResp response = service.getStatement(clientID, sessionUUID);
			StatementRespDTO responseDTO = convertToAccountStatementRespDTO(response);

			HttpStatus httpStatus = HttpStatus.OK;
			switch (responseDTO.getSessionStatus())
			{
				case EXPIRED:
				case DOES_NOT_EXIST:
					httpStatus = HttpStatus.UNAUTHORIZED;
					break;

				default:
					httpStatus = HttpStatus.OK;
					break;
			}

			return new ResponseEntity<String>(Util.asJsonString(responseDTO), httpStatus);
		}

		// Error handling
		// --------------
		catch (SimpleBankServiceException ex)
		{
			StatementRespDTO responseDTO = new StatementRespDTO(null,
					StatementRespDTO.SessionStatus.UNDEFINED);
			return new ResponseEntity<String>(Util.asJsonString(responseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
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
	public @ResponseBody ResponseEntity<String>
			doDeposit (@Valid @RequestBody(required = true) DepositReqDTO depositReqDTO )
	{
		try
		{
			Movement deposit = convertToDeposit(depositReqDTO.getDepositDTO());
			UUID sessionUUID = UUID.fromString(depositReqDTO.getSessionID());

			MovementResp depositResp = service.doDeposit(deposit, sessionUUID);

			HttpStatus status;
			switch (depositResp.getSessionStatus())
			{
				case EXPIRED:
				case DOES_NOT_EXIST:
					status = HttpStatus.UNAUTHORIZED;
					break;

				default:
					status = HttpStatus.CREATED;
					if (depositResp.getMovement().getClientID() == 0)
						status = HttpStatus.CONFLICT;
					break;
			}

			DepositRespDTO depositRespDTO = convertToDepositRespDTO(depositResp);
			return new ResponseEntity<String>(Util.asJsonString(depositRespDTO), status);
		}

		// Error handling
		// --------------
		catch (Exception ex)
		{
			DepositRespDTO depositRespDTO = new DepositRespDTO(null, DepositRespDTO.SessionStatus.UNDEFINED);
			return new ResponseEntity<String>(Util.asJsonString(depositRespDTO),
					HttpStatus.INTERNAL_SERVER_ERROR);
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
	public @ResponseBody ResponseEntity<String>
			doWithdraw (@Valid @RequestBody(required = true) WithdrawReqDTO withdrawReq )
	{
		try
		{
			Movement withdraw = convertToWithdraw(withdrawReq.getWithdrawDTO());
			UUID sessionUUID = UUID.fromString(withdrawReq.getSessionID());

			MovementResp withdrawResp = service.doWithdraw(withdraw, sessionUUID);
			WithdrawRespDTO withdrawRespDTO = convertToWithdrawRespDTO(withdrawResp);

			HttpStatus status;
			switch (withdrawResp.getSessionStatus())
			{
				case EXPIRED:
				case DOES_NOT_EXIST:
					status = HttpStatus.UNAUTHORIZED;
					break;

				default:
					status = HttpStatus.CREATED;
					if (withdrawRespDTO.getWithdraw().getClientID() == 0)
						status = HttpStatus.CONFLICT;
					break;
			}

			return new ResponseEntity<String>(Util.asJsonString(withdrawRespDTO), status);
		}

		// Error handling
		// --------------
		catch (Exception ex)
		{
			WithdrawRespDTO withdrawRespDTO = new WithdrawRespDTO(null, WithdrawRespDTO.SessionStatus.UNDEFINED);
			return new ResponseEntity<String>(Util.asJsonString(withdrawRespDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// ---------------------------------------------------------------------------------- convertToAccountBalanceRespDTO
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Converts an entity account balance into a DTO account balance.
	 * 
	 * @param balanceResp The entity account balance to be converted.
	 * @return The DTO account balance converted.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	private BalanceRespDTO convertToAccountBalanceRespDTO (BalanceResp balanceResp )
	{
		BalanceRespDTO balanceRespDTO = modelMapper.map(balanceResp, BalanceRespDTO.class);

		if (balanceRespDTO.getBalance() != null)
			balanceRespDTO.getBalance().setDate(balanceResp.getBalance().getDate(), DATE_FORMAT, TIME_ZONE);

		return balanceRespDTO;
	}
	
	// -------------------------------------------------------------------------------- convertToAccountStatementRespDTO
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Converts an entity account statement into a DTO account statement.
	 * 
	 * @param statementResp The entity account statement to be converted.
	 * @return The DTO account statement converted.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	private StatementRespDTO convertToAccountStatementRespDTO (StatementResp statementResp )
	{
		StatementRespDTO statementRespDTO = modelMapper.map(statementResp, StatementRespDTO.class);

		if (statementRespDTO.getStatement() != null)
		{
			statementRespDTO.getStatement().setSince(statementResp.getStatement().getSince(), DATE_FORMAT, TIME_ZONE);
			statementRespDTO.getStatement().setUntil(statementResp.getStatement().getUntil(), DATE_FORMAT, TIME_ZONE);

			statementRespDTO.getStatement().setMovementsDTO(new HashSet<>());
			for (Movement movement : statementResp.getStatement().getMovements())
			{
				MovementDTO movementDTO = modelMapper.map(movement, MovementDTO.class);
				movementDTO.setDate(movement.getTime(), DATE_FORMAT, TIME_ZONE);
				statementRespDTO.getStatement().getMovementsDTO().add(movementDTO);
			}
		}

		return statementRespDTO;
	}
	
	// ------------------------------------------------------------------------------------------------ convertToDeposit
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Converts a DTO deposit object into an entity one.
	 * 
	 * @param depositDTO The DTO deposit object to be converted.
	 * @return The entity object converted.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	private Movement convertToDeposit (DepositDTO depositDTO )
		throws ParseException
	{
		// Movement deposit = modelMapper.map(depositDTO, Movement.class);
		Movement deposit = new Movement();
		deposit.setAmount(depositDTO.getAmount());
		deposit.setClientID(depositDTO.getClientID());
		deposit.setTime(depositDTO.getDateTime(DATE_FORMAT, TIME_ZONE));
		return deposit;
	}

	// ----------------------------------------------------------------------------------------- convertToDepositRespDTO
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Converts an entity deposit object into a DTO one.
	 * 
	 * @param depositResp The entity deposit object to be converted.
	 * @return The DTO object converted.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	private DepositRespDTO convertToDepositRespDTO (MovementResp depositResp )
	{
		DepositRespDTO depositRespDTO = modelMapper.map(depositResp, DepositRespDTO.class);
		if (depositResp.getMovement() != null)
		{
			depositRespDTO.setDeposit(modelMapper.map(depositResp.getMovement(), DepositDTO.class));

			if (depositRespDTO.getDeposit() != null)
				depositRespDTO.getDeposit().setDateTime(depositResp.getMovement().getTime(), DATE_FORMAT, TIME_ZONE);
		}
		return depositRespDTO;
	}

	// ----------------------------------------------------------------------------------------------- convertToWithdraw
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Converts a DTO withdraw object into a transaction entity one.
	 * 
	 * @param withdrawDTO The DTO withdraw object to be converted.
	 * @return The entity object converted.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	private Movement convertToWithdraw (WithdrawDTO withdrawDTO )
		throws ParseException
	{
		//Movement withdraw = modelMapper.map(withdrawDTO, Movement.class);
		Movement withdraw = new Movement();
		withdraw.setAmount(withdrawDTO.getAmount());
		withdraw.setClientID(withdrawDTO.getClientID());
		withdraw.setTime(withdrawDTO.getDateTime(DATE_FORMAT, TIME_ZONE));
		return withdraw;
	}

	// ---------------------------------------------------------------------------------------- convertToWithdrawRespDTO
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * Converts a TransactionRespDTO entity object into a WithdrawRespDTO one.
	 * 
	 * @param withdrawResp The entity withdraw object to be converted.
	 * @return The DTO object converted.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	private WithdrawRespDTO convertToWithdrawRespDTO (MovementResp withdrawResp )
	{
		WithdrawRespDTO withdrawRespDTO = modelMapper.map(withdrawResp, WithdrawRespDTO.class);
		if (withdrawResp.getMovement() != null)
		{
			withdrawRespDTO.setWithdraw(modelMapper.map(withdrawResp.getMovement(), WithdrawDTO.class));
			if (withdrawRespDTO.getWithdraw() != null)
				withdrawRespDTO.getWithdraw().setDateTime(withdrawResp.getMovement().getTime(), DATE_FORMAT, TIME_ZONE);
		}
		return withdrawRespDTO;
	}
}
