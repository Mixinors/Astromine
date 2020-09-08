package com.github.chainmailstudios.astromine.discoveries.datagen.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.ModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.GenericBlockModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.GenericItemModelGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.SimpleBlockItemModelGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.SimpleBlockItemModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.ColumnBlockSetModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.GenericBlockSetModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.GenericItemSetModelGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesItems;

public class AstromineDiscoveriesModelStateGenerators extends AstromineModelStateGenerators {
	public final ModelStateGenerator ASTEROID_ORE = register(new ColumnBlockSetModelStateGenerator(MaterialItemType.ASTEROID_ORE, AstromineCommon.identifier("block/asteroid_stone")));

	public final ModelStateGenerator ASTEROID_CLUSTER = register(new GenericItemSetModelGenerator(MaterialItemType.ASTEROID_CLUSTER));

	public final ModelStateGenerator MOON_ORE = register(new GenericBlockSetModelStateGenerator(MaterialItemType.MOON_ORE));

	public final ModelStateGenerator STANDARD_ITEMS = register(new GenericItemModelGenerator(
			AstromineDiscoveriesItems.SPACE_SUIT_HELMET,
			AstromineDiscoveriesItems.SPACE_SUIT_CHESTPLATE,
			AstromineDiscoveriesItems.SPACE_SUIT_LEGGINGS,
			AstromineDiscoveriesItems.SPACE_SUIT_BOOTS,
			AstromineDiscoveriesItems.SPACE_SLIME_BALL,
			AstromineDiscoveriesItems.SPACE_SLIME_SPAWN_EGG
	));

	public final ModelStateGenerator STANDARD_BLOCKS = register(new GenericBlockModelStateGenerator(
			AstromineDiscoveriesBlocks.ASTEROID_STONE,
			AstromineDiscoveriesBlocks.BLAZING_ASTEROID_STONE,
			AstromineDiscoveriesBlocks.MOON_STONE,
			AstromineDiscoveriesBlocks.VULCAN_STONE
	));

	public final ModelStateGenerator CUSTOM_MODEL_BLOCKS = register(new SimpleBlockItemModelStateGenerator(
			AstromineDiscoveriesBlocks.ALTAR,
			AstromineDiscoveriesBlocks.ALTAR_PEDESTAL,
			AstromineDiscoveriesBlocks.MARTIAN_SOIL,
			AstromineDiscoveriesBlocks.MARTIAN_STONE
	));

	public final ModelStateGenerator CUSTOM_MODEL_AND_STATE_BLOCKS = register(new SimpleBlockItemModelGenerator(
			AstromineDiscoveriesBlocks.ASTEROID_STONE_SLAB,
			AstromineDiscoveriesBlocks.ASTEROID_STONE_STAIRS,
			AstromineDiscoveriesBlocks.MOON_STONE_SLAB,
			AstromineDiscoveriesBlocks.MOON_STONE_STAIRS,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.VULCAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.VULCAN_STONE_STAIRS
	));

	public final ModelStateGenerator SPACE_SUIT = register(new GenericItemModelGenerator(
		AstromineDiscoveriesItems.SPACE_SUIT_HELMET,
		AstromineDiscoveriesItems.SPACE_SUIT_CHESTPLATE,
		AstromineDiscoveriesItems.SPACE_SUIT_LEGGINGS,
		AstromineDiscoveriesItems.SPACE_SUIT_BOOTS
	));

	public static void initialize() {

	}
}
