package com.github.chainmailstudios.astromine.common.registry;

import com.github.chainmailstudios.astromine.common.registry.base.UniRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

public class ConveyorBlockBlacklistRegistry extends UniRegistry<Item, Pair<Float, Boolean>> {
	public static final ConveyorBlockBlacklistRegistry INSTANCE = new ConveyorBlockBlacklistRegistry();

	private ConveyorBlockBlacklistRegistry() {
	}
}
