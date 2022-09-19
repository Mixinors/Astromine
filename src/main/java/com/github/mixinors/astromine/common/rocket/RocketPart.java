package com.github.mixinors.astromine.common.rocket;

import net.minecraft.item.Item;

public abstract class RocketPart<T extends Item> {
	private final T item;
	
	public RocketPart(T item) {
		this.item = item;
	}
	
	public T asItem() {
		return item;
	}
}
