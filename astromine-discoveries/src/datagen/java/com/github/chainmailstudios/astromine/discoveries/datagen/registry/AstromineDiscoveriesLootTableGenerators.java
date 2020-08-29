package com.github.chainmailstudios.astromine.discoveries.datagen.registry;

import com.github.chainmailstudios.astromine.datagen.generator.loottable.LootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.onetime.DropSelfLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.set.FortuneOreSetLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineLootTableGenerators;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;

public class AstromineDiscoveriesLootTableGenerators extends AstromineLootTableGenerators {
	public final LootTableGenerator ASTEROID_ORE = register(new FortuneOreSetLootTableGenerator(MaterialItemType.ASTEROID_ORE, MaterialItemType.ASTEROID_CLUSTER));

	public final LootTableGenerator MISC = register(new DropSelfLootTableGenerator(AstromineDiscoveriesBlocks.ALTAR, AstromineDiscoveriesBlocks.ALTAR_PEDESTAL));
}
