package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class AstromineItemGroups {
	public static final ItemGroup ASTROMINE = FabricItemGroupBuilder.build(AstromineCommon.identifier("astromine"), () -> new ItemStack(AstromineBlocks.ASTEROID_ASTERITE_ORE));

	public static void initialize() {
		// Unused.
	}
}
