/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.datagen.registry;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.generator.loottable.onetime.OneTimeLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.set.SetLootTableGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialSet;
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
					AMCommon.LOGGER.info("Loot table generation of " + set.getName() + " succeeded, with generator " + generator.getGeneratorName() + ".");
				}
			} catch (Exception exception) {
				AMCommon.LOGGER.error("Loot table generation of " + set.getName() + " failed, with generator " + generator.getGeneratorName() + ".");
				AMCommon.LOGGER.error(exception.getMessage());
			}
		});
	}

	private void generateOneTimeLootTables(LootTableData lootTables) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(lootTables));
	}
}
