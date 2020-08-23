package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.AstromineItemGroups;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class AstromineTechnologiesItemGroups extends AstromineItemGroups {
	public static final ItemGroup TECHNOLOGIES = register("technologies", () -> AstromineTechnologiesItems.ADVANCED_CIRCUIT);

	public static void initialize() {

	}
}
