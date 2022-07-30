package com.github.mixinors.astromine.common.entity.rocket.part.base;

public abstract class RocketPart<T> {
	private final T item;
	
	public RocketPart(T item) {
		this.item = item;
	}
	
	public T asItem() {
		return item;
	}
}
