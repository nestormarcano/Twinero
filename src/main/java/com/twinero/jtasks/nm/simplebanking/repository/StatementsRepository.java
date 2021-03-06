package com.twinero.jtasks.nm.simplebanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.beans.StatementDAO;

/**
 * The statements entity repository.
 * @author Nestor Marcano
 */
// --------------------------------------------------------------------------------------------------------------------
@Repository
public interface StatementsRepository extends JpaRepository<StatementDAO, Long>
{
}
