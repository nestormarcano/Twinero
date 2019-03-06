package com.twinero.jtasks.nm.simplebanking.repository.specification;

import java.util.Objects;

/**
 * This is the specification which always returns `false`.
 * 
 * @param <T> The type of entity for which the specification is defined.
 */
final class AlwaysFalseSpecification<T> extends NullaryCompositeSpecification<T> {

	private final static AlwaysFalseSpecification<?> INSTANCE = new AlwaysFalseSpecification<>();

	private AlwaysFalseSpecification() {}

	static AlwaysFalseSpecification<?> instance() {
		return INSTANCE;
	}
	@Override
	public boolean isSatisfiedBy(T candidate) {
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof AlwaysFalseSpecification<?>) ? true : false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getClass());
	}
}
