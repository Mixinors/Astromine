package com.github.chainmailstudios.astromine.client.registry;

import net.minecraft.item.Item;

import com.github.chainmailstudios.astromine.client.ItemRenderer;
import com.github.chainmailstudios.astromine.common.registry.base.BiDirectionalRegistry;

public class ItemRendererRegistry extends BiDirectionalRegistry<Item, ItemRenderer<?>> {
	public static final ItemRendererRegistry INSTANCE = new ItemRendererRegistry();

	private ItemRendererRegistry() {
		// Locked.
	}
}
