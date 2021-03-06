package com.twinero.jtasks.nm.simplebanking.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.annotation.Validated;

import com.twinero.jtasks.nm.simplebanking.repository.MovementsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.SessionsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.SignupsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.StatementsRepository;
import com.twinero.jtasks.nm.simplebanking.repository.beans.StatementDAO;
import com.twinero.jtasks.nm.simplebanking.repository.beans.MovementDAO;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SessionDAO;
import com.twinero.jtasks.nm.simplebanking.repository.beans.SignDAO;
import com.twinero.jtasks.nm.simplebanking.repository.exception.SimpleBankServiceException;
import com.twinero.jtasks.nm.simplebanking.service.beans.Balance;
import com.twinero.jtasks.nm.simplebanking.service.beans.BalanceResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.Statement;
import com.twinero.jtasks.nm.simplebanking.service.beans.StatementResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.MovementResp;
import com.twinero.jtasks.nm.simplebanking.service.beans.Session;
import com.twinero.jtasks.nm.simplebanking.service.beans.Movement;

/**
 * Implements the service Simple Bank application layer.
 * @author Nestor Marcano.
 */
// --------------------------------------------------------------------------------------------------------------------
@Service
@Validated
public class SimpleBankServiceImpl implements SimpleBankService
{
	@Autowired
	private SignupsRepository signupsRepository;

	@Autowired
	private SessionsRepository sessionsRepository;

	@Autowired
	private MovementsRepository movementsRepository;

	@Autowired
	private StatementsRepository statementsRepository;

	private ModelMapper modelMapper;

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------- SimpleBankServiceImpl
	/**
	 * Default constructor.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	public SimpleBankServiceImpl ()
	{
		this.modelMapper = modelMapper();
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------- modelMapper
	/**
	 * Returns a new instance of the mapper.
	 * @return A new instance of the mapper.
	 */
	// ----------------------------------------------------------------------------------------------------- modelMapper
	private static ModelMapper modelMapper ()
	{
		return new ModelMapper();
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------- signup
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public SignDAO signup (@Valid SignDAO sign )
	{
		return signupsRepository.save(sign);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------- login
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public Session login (@Valid Session session )
		throws CannotCreateTransactionException
	{
		SignDAO toFindSignDAO = new SignDAO(session.getEmail());
		Optional<SignDAO> foundOptionalSignDAO = signupsRepository.findOne(Example.of(toFindSignDAO));

		if (foundOptionalSignDAO.isPresent())
		{
			SignDAO foundSignDAO = foundOptionalSignDAO.get();
			if (foundSignDAO.getPassword().equals(session.getPassword()))
			{
				SessionDAO sessionDAO = new SessionDAO(foundSignDAO);
				sessionDAO = sessionsRepository.save(sessionDAO);

				session = new Session(sessionDAO.getSessionID().toString());
				session.setClientID(foundSignDAO.getSignID());
				session.setEmail(foundSignDAO.getEmail());
				session.setSessionStatus(Session.Status.OK);
				return session;
			}
		}

		session = new Session(Session.Status.UNAUTHORIZED);
		session.setEmail(toFindSignDAO.getEmail());
		return session;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------ getBalance
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public BalanceResp getBalance (	long clientID,
												UUID sessionID )
		throws SimpleBankServiceException
	{
		Optional<SessionDAO> optionalSessionDAO = sessionsRepository.findById(sessionID);
		if (optionalSessionDAO.isPresent())
		{
			SessionDAO sessionDAO = optionalSessionDAO.get();

			if (isExpiredSession(sessionDAO))
				return new BalanceResp(new Balance(), BalanceResp.SessionStatus.EXPIRED);

			Calendar cal = new GregorianCalendar();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) - 1;
			if (month == -1)
			{
				month = 11;
				year--;
			}
			cal = new GregorianCalendar(year, month, 1, 0, 0, 0);

			StatementDAO example = new StatementDAO(clientID, year, month);
			Optional<StatementDAO> optionalStatementDAO = statementsRepository.findOne(Example.of(example));

			StatementDAO statementDAO;
			if (optionalStatementDAO.isPresent())
			{
				statementDAO = optionalStatementDAO.get();
			}
			else
			{
				statementDAO = new StatementDAO();
				statementDAO.setFinalAmount(BigDecimal.ZERO);
			}

			Date since = cal.getTime();

			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal = new GregorianCalendar(year, month, lastDay, 23, 59, 59);
			cal.set(Calendar.MILLISECOND, 999);

			Date until = cal.getTime();

			List<MovementDAO> movementsDAO = movementsRepository
					.findByCustomerAndTimeBetween(clientID, since, until);

			BigDecimal available = new BigDecimal(statementDAO.getFinalAmount().doubleValue());

			for (MovementDAO movementDAO : movementsDAO)
			{
				available = available.add(movementDAO.getType() == MovementDAO.Type.DEPOSIT
						? movementDAO.getAmount()
						: movementDAO.getAmount().negate());

				available = available.add(movementDAO.getTax().negate());
			}

			available = available.setScale(2, BigDecimal.ROUND_UP);

			Balance balance = new Balance();
			balance.setAvailable(available);
			balance.setTotal(available);
			balance.setBlocked(new BigDecimal(0));
			balance.setDeferred(new BigDecimal(0));
			balance.setClientID(clientID);
			balance.setDate(new Date());

			return new BalanceResp(balance, BalanceResp.SessionStatus.OK);
		}
		else return new BalanceResp(new Balance(), BalanceResp.SessionStatus.DOES_NOT_EXIST);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------- getStatement
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public StatementResp getStatement (	long clientID,
													UUID sessionID )
		throws SimpleBankServiceException
	{
		try
		{
			Optional<SessionDAO> optionalSessionDAO = sessionsRepository.findById(sessionID);
			if (optionalSessionDAO.isPresent())
			{
				SessionDAO sessionDAO = optionalSessionDAO.get();

				if (isExpiredSession(sessionDAO))
					return new StatementResp(null, StatementResp.SessionStatus.EXPIRED);

				Calendar cal = new GregorianCalendar();
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) - 1;
				if (month == -1)
				{
					month = 11;
					year--;
				}
				cal = new GregorianCalendar(year, month, 1, 0, 0, 0);

				Date since = cal.getTime();

				int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				cal = new GregorianCalendar(year, month, lastDay, 23, 59, 59);
				cal.set(Calendar.MILLISECOND, 999);

				Date until = cal.getTime();

				StatementDAO example = new StatementDAO(clientID, year, month);
				Optional<StatementDAO> optionalStatementDAO = statementsRepository.findOne(Example.of(example));

				Statement statement;
				if (optionalStatementDAO.isPresent())
				{
					StatementDAO statementDAO = optionalStatementDAO.get();
					statement = modelMapper.map(statementDAO, Statement.class);

					statement.setSince(since);
					statement.setUntil(until);
					statement.setClientID(clientID);
				}
				else
				{
					statement = new Statement();
				}

				List<MovementDAO> movementsDAO = movementsRepository
						.findByCustomerAndTimeBetween(clientID, since, until);

				Set<Movement> movements = new HashSet<>();
				for (MovementDAO movementDAO : movementsDAO)
				{
					Movement movement = new Movement();
					movement.setAmount(movementDAO.getAmount());
					movement.setClientID(movementDAO.getClientID());
					movement.setDescription(movementDAO.getDescription());
					movement.setMovementID(movementDAO.getMovementID());
					movement.setReference(movementDAO.getReference());
					movement.setTax(movementDAO.getTax());
					movement.setTime(movementDAO.getTime());
					movement
							.setType(movementDAO.getType() == MovementDAO.Type.DEPOSIT
									? Movement.Type.DEPOSIT
									: Movement.Type.WITHDRAW);

					movements.add(movement);
				}

				statement.setMovements(movements);
				return new StatementResp(statement, StatementResp.SessionStatus.OK);
				// }
				// else return new StatementResp(new Statement(), StatementResp.SessionStatus.OK);
			}
			else return new StatementResp(null, StatementResp.SessionStatus.DOES_NOT_EXIST);
		}

		// Error handling
		// --------------
		catch (Exception ex)
		{
			throw new SimpleBankServiceException(ex);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------- doDeposit
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public MovementResp doDeposit (	Movement deposit,
												UUID sessionID )
		throws SimpleBankServiceException
	{
		deposit.setType(Movement.Type.DEPOSIT);
		return doTransaction(deposit, sessionID);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------ doWithdraw
	/**
	 * {@inheritDoc}
	 */
	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public MovementResp doWithdraw (	Movement withdraw,
												UUID sessionID )
		throws SimpleBankServiceException
	{
		withdraw.setType(Movement.Type.WITHDRAW);
		return doTransaction(withdraw, sessionID);
	}

	// -----------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------- doTransaction
	/**
	 * 
	 * @param transaction The transaction data.
	 * @param sessionID The session ID.
	 * @return A MovementResp object.
	 * @throws SimpleBankServiceException An object with the error data.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	private MovementResp doTransaction (Movement transaction,
													UUID sessionID )
		throws SimpleBankServiceException
	{
		Optional<SessionDAO> optionalSessionDAO = sessionsRepository.findById(sessionID);
		if (optionalSessionDAO.isPresent())
		{
			SessionDAO sessionDAO = optionalSessionDAO.get();

			if (isExpiredSession(sessionDAO))
				return new MovementResp(new Movement(), MovementResp.SessionStatus.EXPIRED);

			MovementDAO transactionDAO = modelMapper.map(transaction, MovementDAO.class);
			transactionDAO = movementsRepository.save(transactionDAO);

			transaction = modelMapper.map(transactionDAO, Movement.class);
			return new MovementResp(transaction, MovementResp.SessionStatus.OK);
		}
		else
		{
			return new MovementResp(new Movement(), MovementResp.SessionStatus.DOES_NOT_EXIST);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------ isExpiredSession
	/**
	 * Indicates if the session is expired.
	 * @param sessionDAO The session
	 * @return false if session is not expired else true.
	 */
	// -----------------------------------------------------------------------------------------------------------------
	private boolean isExpiredSession (SessionDAO sessionDAO )
	{
		return sessionDAO.getTime().before(new Date());
	}
}
