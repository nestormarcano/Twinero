package com.twinero.jtasks.nm.simplebanking.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.beans.SessionDAO;

/**
 * The sessions entity repository.
 * @author Nestor Marcano
 */
// --------------------------------------------------------------------------------------------------------------------
@Repository
public interface SessionsRepository extends JpaRepository<SessionDAO, UUID>
{
}
