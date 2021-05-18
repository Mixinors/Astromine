package com.github.mixinors.astromine.common.component.general.base;

import com.github.mixinors.astromine.common.component.AutoSyncedComponent;
import net.minecraft.item.ItemStack;

/**
 * A {@link SimpleDirectionalItemComponent} that synchronizes itself
 * automatically.
 */
public class SimpleAutoSyncedDirectionalItemComponent extends SimpleDirectionalItemComponent implements AutoSyncedComponent {
	/** Instantiates a {@link SimpleDirectionalItemComponent}. */
	public <V> SimpleAutoSyncedDirectionalItemComponent(V v, int size) {
		super(v, size);
	}
	
	/** Instantiates a {@link SimpleDirectionalItemComponent}. */
	public <V> SimpleAutoSyncedDirectionalItemComponent(V v, ItemStack... stacks) {
		super(v, stacks);
	}
}
