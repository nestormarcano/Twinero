package com.twinero.jtasks.nm.simplebanking.repository;

public interface Mapper<From, To> {
   To map(From from);
}
