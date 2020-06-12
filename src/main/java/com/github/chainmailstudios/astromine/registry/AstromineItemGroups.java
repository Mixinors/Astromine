package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class AstromineItemGroups {

	public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier("astromine", "group"), () -> new ItemStack(Items.OBSIDIAN));

	public static void initialize() {
		// Unused.
	}
}
