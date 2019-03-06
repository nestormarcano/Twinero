package com.twinero.jtasks.nm.simplebanking.repository.specification;

import java.util.ArrayList;
import java.util.Arrays;

import com.twinero.jtasks.nm.simplebanking.repository.specification.protocols.Immutable;

/**
 * The specification with one component.
 *
 * @param <T> The type of entity for which the specification is defined.
 */
@Immutable
abstract class UnaryCompositeSpecification<T> extends CompositeSpecification<T> {
	
	/**
	 * Creates the unary composite specification.
	 * 
	 * @param specification The inner specification.
	 */
	protected UnaryCompositeSpecification(final Specification<T> specification) {
		super(new ArrayList<Specification<T>>(Arrays.asList(specification)));
	}
	
	/**
	 * Gets the inner specification.
	 * 
	 * @return The inner specification.
	 */
	public Specification<T> getSpecification() {
		return getSpecifications().get(0);
	}
}
