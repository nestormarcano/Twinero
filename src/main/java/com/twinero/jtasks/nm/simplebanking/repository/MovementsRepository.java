package com.twinero.jtasks.nm.simplebanking.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twinero.jtasks.nm.simplebanking.repository.beans.MovementDAO;

@Repository
public interface MovementsRepository extends JpaRepository<MovementDAO, Long>
{
	@Query("select m from MovementDAO m"
			+ " where m.customer.signID = :customer_ID and m.time >= :timeStart and m.time <= :timeEnd")
	public List<MovementDAO> findByCustomerAndTimeBetween (	@Param("customer_ID") long customer_ID,
																				@Param("timeStart") Date timeStart,
																				@Param("timeEnd") Date timeEnd );
}
