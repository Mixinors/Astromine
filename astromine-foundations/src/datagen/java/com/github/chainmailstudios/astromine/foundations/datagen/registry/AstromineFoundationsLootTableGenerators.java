package com.github.chainmailstudios.astromine.foundations.datagen.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.LootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.onetime.DropSelfLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.set.DropSelfSetLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.set.FortuneOreSetLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineLootTableGenerators;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;

public class AstromineFoundationsLootTableGenerators extends AstromineLootTableGenerators {
	public final LootTableGenerator BLOCK = register(new DropSelfSetLootTableGenerator(MaterialItemType.BLOCK));
	public final LootTableGenerator ORE = register(new DropSelfSetLootTableGenerator(MaterialItemType.ORE));

	public final LootTableGenerator METEOR_ORE = register(new FortuneOreSetLootTableGenerator(MaterialItemType.METEOR_ORE, MaterialItemType.METEOR_CLUSTER));
	public final LootTableGenerator ASTEROID_ORE = register(new FortuneOreSetLootTableGenerator(MaterialItemType.ASTEROID_ORE, MaterialItemType.ASTEROID_CLUSTER));

	public final LootTableGenerator MISC = register(new DropSelfLootTableGenerator(AstromineFoundationsBlocks.ALTAR, AstromineFoundationsBlocks.ITEM_DISPLAYER));
}
