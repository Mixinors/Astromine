package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.common.item.UncoloredSpawnEggItem;
import com.github.chainmailstudios.astromine.registry.AstromineEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.minecraft.item.Item;

public class AstromineDiscoveriesItems extends AstromineItems {
	public static final Item SPACE_SLIME_SPAWN_EGG = register("space_slime_spawn_egg", new UncoloredSpawnEggItem(AstromineDiscoveriesEntityTypes.SPACE_SLIME, AstromineDiscoveriesItems.getBasicSettings()));

	public static final Item SPACE_SLIME_BALL = register("space_slime_ball", new Item(AstromineDiscoveriesItems.getBasicSettings()));

	public static void initialize() {

	}

	public static Item.Settings getBasicSettings() {
		return AstromineItems.getBasicSettings().group(AstromineDiscoveriesItemGroups.DISCOVERIES);
	}
}
