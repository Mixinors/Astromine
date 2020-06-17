package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;

public class AstromineItemGroups {
	public static final ItemGroup ASTROMINE = FabricItemGroupBuilder.build(AstromineCommon.identifier("astromine"), () -> new ItemStack(AstromineBlocks.ASTERITE_ORE));

	public static void initialize() {
		// Unused.
	}
}
