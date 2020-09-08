package com.github.chainmailstudios.astromine.discoveries.datagen.registry;

import com.github.chainmailstudios.astromine.datagen.generator.loottable.LootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.onetime.DropSelfLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.onetime.SlabLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.set.DropSelfSetLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.set.FortuneOreSetLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineLootTableGenerators;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;

public class AstromineDiscoveriesLootTableGenerators extends AstromineLootTableGenerators {
	public final LootTableGenerator ASTEROID_ORE = register(new FortuneOreSetLootTableGenerator(MaterialItemType.ASTEROID_ORE, MaterialItemType.ASTEROID_CLUSTER));
	public final LootTableGenerator MOON_ORE = register(new DropSelfSetLootTableGenerator(MaterialItemType.MOON_ORE));

	public final LootTableGenerator DROP_SELF = register(new DropSelfLootTableGenerator(
			AstromineDiscoveriesBlocks.ALTAR,
			AstromineDiscoveriesBlocks.ALTAR_PEDESTAL,
			AstromineDiscoveriesBlocks.ASTEROID_STONE,
			AstromineDiscoveriesBlocks.ASTEROID_STONE_STAIRS,
			AstromineDiscoveriesBlocks.ASTEROID_STONE_WALL,
			AstromineDiscoveriesBlocks.BLAZING_ASTEROID_STONE,
			AstromineDiscoveriesBlocks.MOON_STONE,
			AstromineDiscoveriesBlocks.MOON_STONE_STAIRS,
			AstromineDiscoveriesBlocks.MOON_STONE_WALL,
			AstromineDiscoveriesBlocks.MARTIAN_SOIL,
			AstromineDiscoveriesBlocks.MARTIAN_STONE,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_WALL,
			AstromineDiscoveriesBlocks.VULCAN_STONE,
			AstromineDiscoveriesBlocks.VULCAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.VULCAN_STONE_WALL
	));

	public final LootTableGenerator SLABS = register(new SlabLootTableGenerator(
			AstromineDiscoveriesBlocks.ASTEROID_STONE_SLAB,
			AstromineDiscoveriesBlocks.MOON_STONE_SLAB,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.VULCAN_STONE_SLAB
	));

	public static void initialize() {

	}
}
