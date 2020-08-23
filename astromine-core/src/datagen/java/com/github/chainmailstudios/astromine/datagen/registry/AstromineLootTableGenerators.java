package com.github.chainmailstudios.astromine.datagen.registry;

import com.github.chainmailstudios.astromine.datagen.generator.loottable.onetime.OneTimeLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.loottable.set.SetLootTableGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.OneTimeRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.SetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.ArrayList;
import java.util.List;

public class AstromineLootTableGenerators {
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
		AstromineMaterialSets.getMaterialSets().forEach((set) -> {
			generateSetLootTables(lootTables, set);
		});
		generateOneTimeLootTables(lootTables);
	}

	private void generateSetLootTables(LootTableData lootTables, MaterialSet set) {
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(lootTables, set);
					System.out.println("generated loot table " + generator.getGeneratorName());
				}
			} catch (Exception e) {
				System.out.println("oh fuck loot table bronked for " + set.getName() + " with generator " + generator.getGeneratorName());
				System.out.println(e.getMessage());
			}
		});
	}

	private void generateOneTimeLootTables(LootTableData lootTables) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(lootTables));
	}
}
