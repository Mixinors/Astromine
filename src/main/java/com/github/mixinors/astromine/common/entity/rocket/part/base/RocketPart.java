package com.github.mixinors.astromine.common.entity.rocket.part.base;

import com.github.mixinors.astromine.common.util.data.tier.Tier;

public abstract class RocketPart<T> {
	private final T item;
	
	private final Tier tier;
	
	public RocketPart(T item, Tier tier) {
		this.item = item;
		
		this.tier = tier;
	}
	
	public Tier getTier() {
		return tier;
	}
	
	public T asItem() {
		return item;
	}
}
