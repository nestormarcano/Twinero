package com.twinero.jtasks.nm.simplebanking.repository;

public interface IRepository<T, K>
{
	T get (K id);
	T add (T item );
}