package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import com.github.chainmailstudios.astromine.AstromineCommon;

public class AstromineItemGroups {
	public static final ItemGroup ASTROMINE = FabricItemGroupBuilder.build(AstromineCommon.identifier("astromine"), () -> new ItemStack(AstromineItems.UNIVITE_INGOT));

	public static void initialize() {
		// Unused.
	}
}
