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
			AstromineDiscoveriesBlocks.SMOOTH_ASTEROID_STONE,
			AstromineDiscoveriesBlocks.SMOOTH_ASTEROID_STONE_STAIRS,
			AstromineDiscoveriesBlocks.SMOOTH_ASTEROID_STONE_WALL,
			AstromineDiscoveriesBlocks.POLISHED_ASTEROID_STONE,
			AstromineDiscoveriesBlocks.POLISHED_ASTEROID_STONE_STAIRS,
			AstromineDiscoveriesBlocks.ASTEROID_STONE_BRICKS,
			AstromineDiscoveriesBlocks.ASTEROID_STONE_BRICK_STAIRS,
			AstromineDiscoveriesBlocks.ASTEROID_STONE_BRICK_WALL,
			AstromineDiscoveriesBlocks.BLAZING_ASTEROID_STONE,
			AstromineDiscoveriesBlocks.MOON_STONE,
			AstromineDiscoveriesBlocks.MOON_STONE_STAIRS,
			AstromineDiscoveriesBlocks.MOON_STONE_WALL,
			AstromineDiscoveriesBlocks.MARTIAN_SOIL,
			AstromineDiscoveriesBlocks.MARTIAN_STONE,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_WALL,
			AstromineDiscoveriesBlocks.SMOOTH_MARTIAN_STONE,
			AstromineDiscoveriesBlocks.SMOOTH_MARTIAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.SMOOTH_MARTIAN_STONE_WALL,
			AstromineDiscoveriesBlocks.POLISHED_MARTIAN_STONE,
			AstromineDiscoveriesBlocks.POLISHED_MARTIAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_BRICKS,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_BRICK_STAIRS,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_BRICK_WALL,
			AstromineDiscoveriesBlocks.VULCAN_STONE,
			AstromineDiscoveriesBlocks.VULCAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.VULCAN_STONE_WALL,
			AstromineDiscoveriesBlocks.SMOOTH_VULCAN_STONE,
			AstromineDiscoveriesBlocks.SMOOTH_VULCAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.SMOOTH_VULCAN_STONE_WALL,
			AstromineDiscoveriesBlocks.POLISHED_VULCAN_STONE,
			AstromineDiscoveriesBlocks.POLISHED_VULCAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.VULCAN_STONE_BRICKS,
			AstromineDiscoveriesBlocks.VULCAN_STONE_BRICK_STAIRS,
			AstromineDiscoveriesBlocks.VULCAN_STONE_BRICK_WALL,
			AstromineDiscoveriesBlocks.SPACE_SLIME_BLOCK
	));

	public final LootTableGenerator SLABS = register(new SlabLootTableGenerator(
			AstromineDiscoveriesBlocks.ASTEROID_STONE_SLAB,
			AstromineDiscoveriesBlocks.SMOOTH_ASTEROID_STONE_SLAB,
			AstromineDiscoveriesBlocks.POLISHED_ASTEROID_STONE_SLAB,
			AstromineDiscoveriesBlocks.ASTEROID_STONE_BRICK_SLAB,
			AstromineDiscoveriesBlocks.MOON_STONE_SLAB,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.SMOOTH_MARTIAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.POLISHED_MARTIAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_BRICK_SLAB,
			AstromineDiscoveriesBlocks.VULCAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.SMOOTH_VULCAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.POLISHED_VULCAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.VULCAN_STONE_BRICK_SLAB
	));

	public static void initialize() {

	}
}
