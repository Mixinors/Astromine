package com.github.chainmailstudios.astromine.client.registry;

import net.minecraft.item.Item;

import com.github.chainmailstudios.astromine.client.ItemRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ItemRendererRegistry {
	public static final ItemRendererRegistry INSTANCE = new ItemRendererRegistry();

	private static final Map<Item, ItemRenderer<?>> entries = new HashMap<>();

	private ItemRendererRegistry() {
	}

	public void register(Item[] items, Function<Item, ItemRenderer> supplier) {
		for (int i = 0; i < items.length; ++i) {
			this.register(items[i], supplier.apply(items[i]));
		}
	}

	public void register(Item item, ItemRenderer<?> itemRenderer) {
		entries.put(item, itemRenderer);
	}

	public Item getItem(ItemRenderer<?> itemRenderer) {
		Optional<Map.Entry<Item, ItemRenderer<?>>> renderer = entries.entrySet().stream().filter(entry -> entry.getValue() == itemRenderer).findFirst();
		return (renderer.map(Map.Entry::getKey).orElse(null));
	}

	public ItemRenderer<?> get(Item item) {
		return entries.get(item);
	}
}
