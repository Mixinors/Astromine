package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.item.SuperSpaceSlimeShooterItem;
import com.github.chainmailstudios.astromine.common.item.UncoloredSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AstromineItems {

	public static final UncoloredSpawnEggItem SPACE_SLIME_SPAWN_EGG = register("space_slime_spawn_egg", new UncoloredSpawnEggItem(
			AstromineEntities.SPACE_SLIME,
			new Item.Settings().group(AstromineItemGroups.GROUP)
	));

	public static final SuperSpaceSlimeShooterItem SUPER_SPACE_SLIME_SHOOTER = register("super_space_slime_shooter", new SuperSpaceSlimeShooterItem(new Item.Settings().group(AstromineItemGroups.GROUP)));
	public static final Item SPACE_SLIME_BALL = register("space_slime_ball", new Item(new Item.Settings().group(AstromineItemGroups.GROUP)));

	/**
	 * @param name Name of item instance to be registered
	 * @param item Item instance to be registered
	 * @return Item instanced registered
	 */
	public static <T extends Item> T register(String name, T item) {
		return register(new Identifier(AstromineCommon.MOD_ID, name), item);
	}

	/**
	 * @param name Identifier of item instance to be registered
	 * @param item Item instance to be registered
	 * @return Item instance registered
	 */
	public static <T extends Item> T register(Identifier name, T item) {
		return Registry.register(Registry.ITEM, name, item);
	}

	public static void initialize() {
		// Unused.
	}
}
