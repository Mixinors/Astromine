package com.github.chainmailstudios.astromine.common.multiblock;

import com.github.chainmailstudios.astromine.common.registry.base.BiRegistry;
import net.minecraft.util.Identifier;

public class MultiblockTypeRegistry extends BiRegistry<Identifier, MultiblockType> {
	public static final MultiblockTypeRegistry INSTANCE = new MultiblockTypeRegistry();

	private MultiblockTypeRegistry() {

	}
}
