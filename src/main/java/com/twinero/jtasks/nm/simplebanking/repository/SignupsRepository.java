package com.twinero.jtasks.nm.simplebanking.repository;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.twinero.jtasks.nm.simplebanking.repository.beans.SignDAO;

@Repository
public interface SignupsRepository extends JpaRepository<SignDAO, Long>
{
}
