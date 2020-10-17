package com.github.chainmailstudios.astromine.foundations.datagen.registry;

import net.minecraft.loot.UniformLootTableRange;

import com.github.chainmailstudios.astromine.datagen.generator.loottable.LootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.onetime.DropSelfLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.onetime.SlabLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.set.DropSelfSetLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.set.FortuneMultiOreSetLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineLootTableGenerators;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;

public class AstromineFoundationsLootTableGenerators extends AstromineLootTableGenerators {
	public final LootTableGenerator BLOCK = register(new DropSelfSetLootTableGenerator(MaterialItemType.BLOCK));
	public final LootTableGenerator ORE = register(new DropSelfSetLootTableGenerator(MaterialItemType.ORE));

	public final LootTableGenerator DROP_SELF = register(new DropSelfLootTableGenerator(
			AstromineFoundationsBlocks.METEOR_STONE,
			AstromineFoundationsBlocks.METEOR_STONE_STAIRS,
			AstromineFoundationsBlocks.METEOR_STONE_WALL,
			AstromineFoundationsBlocks.SMOOTH_METEOR_STONE,
			AstromineFoundationsBlocks.SMOOTH_METEOR_STONE_STAIRS,
			AstromineFoundationsBlocks.SMOOTH_METEOR_STONE_WALL,
			AstromineFoundationsBlocks.POLISHED_METEOR_STONE,
			AstromineFoundationsBlocks.POLISHED_METEOR_STONE_STAIRS,
			AstromineFoundationsBlocks.METEOR_STONE_BRICKS,
			AstromineFoundationsBlocks.METEOR_STONE_BRICK_STAIRS,
			AstromineFoundationsBlocks.METEOR_STONE_BRICK_WALL
	));

	public final LootTableGenerator SLABS = register(new SlabLootTableGenerator(
			AstromineFoundationsBlocks.METEOR_STONE_SLAB,
			AstromineFoundationsBlocks.SMOOTH_METEOR_STONE_SLAB,
			AstromineFoundationsBlocks.POLISHED_METEOR_STONE_SLAB,
			AstromineFoundationsBlocks.METEOR_STONE_BRICK_SLAB
	));

	public final LootTableGenerator METEOR_ORE = register(new FortuneMultiOreSetLootTableGenerator(MaterialItemType.METEOR_ORE, MaterialItemType.METEOR_CLUSTER, new UniformLootTableRange(2)));
}
