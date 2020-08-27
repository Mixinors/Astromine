package com.github.chainmailstudios.astromine.common.registry;

import com.github.chainmailstudios.astromine.common.registry.base.UniRegistry;
import com.github.chainmailstudios.astromine.registry.AstromineBlacklistedConveyorBlocks;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

public class BlacklistedConveyorBlockRegistry  extends UniRegistry<Item, Pair<Float, Boolean>> {
	public static final BlacklistedConveyorBlockRegistry INSTANCE = new BlacklistedConveyorBlockRegistry();

	private BlacklistedConveyorBlockRegistry() {
	}
}
