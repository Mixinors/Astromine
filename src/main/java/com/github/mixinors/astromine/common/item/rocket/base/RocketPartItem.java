package com.github.mixinors.astromine.common.item.rocket.base;

import net.minecraft.item.Item;

public abstract class RocketPartItem<T> extends Item {
	protected T part;
	
	public RocketPartItem(Settings settings) {
		super(settings);
	}
	
	public T getPart() {
		return part;
	}
}
