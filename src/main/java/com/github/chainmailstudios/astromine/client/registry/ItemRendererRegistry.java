package com.github.chainmailstudios.astromine.client.registry;

import com.github.chainmailstudios.astromine.client.ItemRenderer;
import com.github.chainmailstudios.astromine.common.registry.AlphaRegistry;
import net.minecraft.item.Item;

public class ItemRendererRegistry extends AlphaRegistry<Item, ItemRenderer<?>> {
	public static final ItemRendererRegistry INSTANCE = new ItemRendererRegistry();

	private ItemRendererRegistry() {
		// Locked.
	}
}
