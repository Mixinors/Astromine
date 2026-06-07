package com.github.mixinors.astromine.common.item.rocket;

import net.minecraft.world.item.Item;

public abstract class RocketPartItem<T> extends Item {
	protected T part;
	
	public RocketPartItem(Properties settings) {
		super(settings);
	}
	
	public T getPart() {
		return part;
	}
}
