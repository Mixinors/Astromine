package com.github.chainmailstudios.astromine.datagen.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.onetime.OneTimeLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.set.SetLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;

import java.util.ArrayList;
import java.util.List;

public abstract class AstromineLootTableGenerators {
	private final List<SetLootTableGenerator> SET_GENERATORS = new ArrayList<>();
	private final List<OneTimeLootTableGenerator> ONE_TIME_GENERATORS = new ArrayList<>();

	public SetLootTableGenerator register(SetLootTableGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public OneTimeLootTableGenerator register(OneTimeLootTableGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public void generateLootTables(LootTableData lootTables) {
		AstromineMaterialSets.getMaterialSets().forEach((set) -> generateSetLootTables(lootTables, set));
		generateOneTimeLootTables(lootTables);
	}

	private void generateSetLootTables(LootTableData lootTables, MaterialSet set) {
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(lootTables, set);
					AstromineCommon.LOGGER.info("Loot table generation of " + set.getName() + " succeeded, with generator " + generator.getGeneratorName() + ".");
				}
			} catch (Exception exception) {
				AstromineCommon.LOGGER.error("Loot table generation of " + set.getName() + " failed, with generator " + generator.getGeneratorName() + ".");
				AstromineCommon.LOGGER.error(exception.getMessage());
			}
		});
	}

	private void generateOneTimeLootTables(LootTableData lootTables) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(lootTables));
	}
}
