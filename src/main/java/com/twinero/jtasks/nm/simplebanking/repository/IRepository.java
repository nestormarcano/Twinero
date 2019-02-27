package com.twinero.jtasks.nm.simplebanking.repository;

import java.util.List;

public interface IRepository<T, K>
{
	List<T> read ();

	T readById (K id );

	T create (T entity );

	T update (T entity );

	T delete (T entity );
}