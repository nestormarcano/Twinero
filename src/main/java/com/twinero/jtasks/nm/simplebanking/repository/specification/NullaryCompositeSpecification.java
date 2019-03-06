package com.twinero.jtasks.nm.simplebanking.repository.specification;

import com.twinero.jtasks.nm.simplebanking.repository.specification.protocols.Immutable;

/**
 * The specification without any component.
 *
 * @param <T> The type of entity for which the specification is defined.
 */
@Immutable
abstract class NullaryCompositeSpecification<T> implements Specification<T> {
	
}
