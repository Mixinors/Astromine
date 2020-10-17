package com.github.chainmailstudios.astromine.foundations.datagen.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.ModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.GenericBlockModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.SimpleBlockItemModelGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.ColumnBlockSetModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.GenericBlockSetModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.GenericItemSetModelGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.HandheldItemSetModelGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;

public class AstromineFoundationsModelStateGenerators extends AstromineModelStateGenerators {
	public final ModelStateGenerator INGOT = register(new GenericItemSetModelGenerator(MaterialItemType.INGOT));
	public final ModelStateGenerator GEM = register(new GenericItemSetModelGenerator(MaterialItemType.GEM));
	public final ModelStateGenerator MISC_RESOURCE = register(new GenericItemSetModelGenerator(MaterialItemType.MISC_RESOURCE));

	public final ModelStateGenerator NUGGET = register(new GenericItemSetModelGenerator(MaterialItemType.NUGGET));
	public final ModelStateGenerator FRAGMENT = register(new GenericItemSetModelGenerator(MaterialItemType.FRAGMENT));

	public final ModelStateGenerator BLOCK = register(new GenericBlockSetModelStateGenerator(MaterialItemType.BLOCK));
	public final ModelStateGenerator ORE = register(new GenericBlockSetModelStateGenerator(MaterialItemType.ORE));

	public final ModelStateGenerator METEOR_ORE = register(new ColumnBlockSetModelStateGenerator(MaterialItemType.METEOR_ORE, AstromineCommon.identifier("block/meteor_stone")));

	public final ModelStateGenerator METEOR_CLUSTER = register(new GenericItemSetModelGenerator(MaterialItemType.METEOR_CLUSTER));

	public final ModelStateGenerator DUST = register(new GenericItemSetModelGenerator(MaterialItemType.DUST));
	public final ModelStateGenerator TINY_DUST = register(new GenericItemSetModelGenerator(MaterialItemType.TINY_DUST));

	public final ModelStateGenerator GEAR = register(new GenericItemSetModelGenerator(MaterialItemType.GEAR));
	public final ModelStateGenerator PLATES = register(new GenericItemSetModelGenerator(MaterialItemType.PLATE));
	public final ModelStateGenerator WIRE = register(new GenericItemSetModelGenerator(MaterialItemType.WIRE));

	public final ModelStateGenerator PICKAXE = register(new HandheldItemSetModelGenerator(MaterialItemType.PICKAXE));
	public final ModelStateGenerator AXE = register(new HandheldItemSetModelGenerator(MaterialItemType.AXE));
	public final ModelStateGenerator SHOVEL = register(new HandheldItemSetModelGenerator(MaterialItemType.SHOVEL));
	public final ModelStateGenerator SWORD = register(new HandheldItemSetModelGenerator(MaterialItemType.SWORD));
	public final ModelStateGenerator HOE = register(new HandheldItemSetModelGenerator(MaterialItemType.HOE));

	public final ModelStateGenerator MATTOCK = register(new HandheldItemSetModelGenerator(MaterialItemType.MATTOCK));
	public final ModelStateGenerator MINING_TOOL = register(new HandheldItemSetModelGenerator(MaterialItemType.MINING_TOOL));

	public final ModelStateGenerator HAMMER = register(new HandheldItemSetModelGenerator(MaterialItemType.HAMMER));
	public final ModelStateGenerator EXCAVATOR = register(new HandheldItemSetModelGenerator(MaterialItemType.EXCAVATOR));

	public final ModelStateGenerator HELMET = register(new GenericItemSetModelGenerator(MaterialItemType.HELMET));
	public final ModelStateGenerator CHESTPLATE = register(new GenericItemSetModelGenerator(MaterialItemType.CHESTPLATE));
	public final ModelStateGenerator LEGGINGS = register(new GenericItemSetModelGenerator(MaterialItemType.LEGGINGS));
	public final ModelStateGenerator BOOTS = register(new GenericItemSetModelGenerator(MaterialItemType.BOOTS));

	public final ModelStateGenerator WRENCH = register(new HandheldItemSetModelGenerator(MaterialItemType.WRENCH));

	public final ModelStateGenerator APPLE = register(new GenericItemSetModelGenerator(MaterialItemType.APPLE));

	public final ModelStateGenerator STANDARD_BLOCKS = register(new GenericBlockModelStateGenerator(
			AstromineFoundationsBlocks.METEOR_STONE,
			AstromineFoundationsBlocks.SMOOTH_METEOR_STONE,
			AstromineFoundationsBlocks.POLISHED_METEOR_STONE,
			AstromineFoundationsBlocks.METEOR_STONE_BRICKS
	));

	public final ModelStateGenerator CUSTOM_MODEL_AND_STATE_BLOCKS = register(new SimpleBlockItemModelGenerator(
			AstromineFoundationsBlocks.METEOR_STONE_SLAB,
			AstromineFoundationsBlocks.METEOR_STONE_STAIRS,
			AstromineFoundationsBlocks.SMOOTH_METEOR_STONE_SLAB,
			AstromineFoundationsBlocks.SMOOTH_METEOR_STONE_STAIRS,
			AstromineFoundationsBlocks.POLISHED_METEOR_STONE_SLAB,
			AstromineFoundationsBlocks.POLISHED_METEOR_STONE_STAIRS,
			AstromineFoundationsBlocks.METEOR_STONE_BRICK_SLAB,
			AstromineFoundationsBlocks.METEOR_STONE_BRICK_STAIRS
	));
}
