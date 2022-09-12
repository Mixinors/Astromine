package com.github.mixinors.astromine.common.rocket;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import org.jetbrains.annotations.Nullable;

public abstract class RocketPart<T extends Item> {
	private final T item;
	
	public RocketPart(T item) {
		this.item = item;
	}
	
	public T asItem() {
		return item;
	}
}
