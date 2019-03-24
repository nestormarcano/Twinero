package com.twinero.jtasks.nm.simplebanking.repository;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.twinero.jtasks.nm.simplebanking.repository.beans.SignDAO;

/**
 * The sign-ups entity repository.<br>
 * A sign-up becomes a customer in this application.
 * 
 * @author Nestor Marcano
 */
// --------------------------------------------------------------------------------------------------------------------
@Repository
public interface SignupsRepository extends JpaRepository<SignDAO, Long>
{
}
