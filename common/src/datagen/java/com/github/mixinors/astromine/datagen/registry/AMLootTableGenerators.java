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
import com.github.mixinors.astromine.datagen.generator.loottable.LootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.onetime.DropSelfLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.onetime.OneTimeLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.onetime.SlabLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.set.DropSelfSetLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.set.FortuneMultiOreSetLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.set.FortuneOreSetLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.set.SetLootTableGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.material.MaterialSet;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;
import net.minecraft.loot.UniformLootTableRange;

import java.util.ArrayList;
import java.util.List;

public class AMLootTableGenerators {
	private final List<SetLootTableGenerator> SET_GENERATORS = new ArrayList<>();
	private final List<OneTimeLootTableGenerator> ONE_TIME_GENERATORS = new ArrayList<>();
	
	public final LootTableGenerator ASTEROID_ORE = register(new FortuneOreSetLootTableGenerator(MaterialItemType.ASTEROID_ORE, MaterialItemType.ASTEROID_CLUSTER));
	
	public final LootTableGenerator DROP_SELF = register(new DropSelfLootTableGenerator(
			AMBlocks.ALTAR.get(),
			AMBlocks.ALTAR_PEDESTAL.get(),
			AMBlocks.ASTEROID_STONE.get(),
			AMBlocks.ASTEROID_STONE_STAIRS.get(),
			AMBlocks.ASTEROID_STONE_WALL.get(),
			AMBlocks.SMOOTH_ASTEROID_STONE.get(),
			AMBlocks.SMOOTH_ASTEROID_STONE_STAIRS.get(),
			AMBlocks.SMOOTH_ASTEROID_STONE_WALL.get(),
			AMBlocks.POLISHED_ASTEROID_STONE.get(),
			AMBlocks.POLISHED_ASTEROID_STONE_STAIRS.get(),
			AMBlocks.ASTEROID_STONE_BRICKS.get(),
			AMBlocks.ASTEROID_STONE_BRICK_STAIRS.get(),
			AMBlocks.ASTEROID_STONE_BRICK_WALL.get(),
			AMBlocks.BLAZING_ASTEROID_STONE.get(),
			AMBlocks.SPACE_SLIME_BLOCK.get(),
			AMBlocks.METEOR_STONE.get(),
			AMBlocks.METEOR_STONE_STAIRS.get(),
			AMBlocks.METEOR_STONE_WALL.get(),
			AMBlocks.SMOOTH_METEOR_STONE.get(),
			AMBlocks.SMOOTH_METEOR_STONE_STAIRS.get(),
			AMBlocks.SMOOTH_METEOR_STONE_WALL.get(),
			AMBlocks.POLISHED_METEOR_STONE.get(),
			AMBlocks.POLISHED_METEOR_STONE_STAIRS.get(),
			AMBlocks.METEOR_STONE_BRICKS.get(),
			AMBlocks.METEOR_STONE_BRICK_STAIRS.get(),
			AMBlocks.METEOR_STONE_BRICK_WALL.get(),
			AMBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR.get(),
			AMBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.get(),
			
			AMBlocks.VENT.get(),
			
			AMBlocks.PRIMITIVE_TANK.get(),
			AMBlocks.BASIC_TANK.get(),
			AMBlocks.ADVANCED_TANK.get(),
			AMBlocks.ELITE_TANK.get(),
			AMBlocks.CREATIVE_TANK.get(),
			
			AMBlocks.PRIMITIVE_SOLID_GENERATOR.get(),
			AMBlocks.BASIC_SOLID_GENERATOR.get(),
			AMBlocks.ADVANCED_SOLID_GENERATOR.get(),
			AMBlocks.ELITE_SOLID_GENERATOR.get(),
			
			AMBlocks.PRIMITIVE_LIQUID_GENERATOR.get(),
			AMBlocks.BASIC_LIQUID_GENERATOR.get(),
			AMBlocks.ADVANCED_LIQUID_GENERATOR.get(),
			AMBlocks.ELITE_LIQUID_GENERATOR.get(),
			
			AMBlocks.PRIMITIVE_ELECTRIC_FURNACE.get(),
			AMBlocks.BASIC_ELECTRIC_FURNACE.get(),
			AMBlocks.ADVANCED_ELECTRIC_FURNACE.get(),
			AMBlocks.ELITE_ELECTRIC_FURNACE.get(),
			
			AMBlocks.PRIMITIVE_ALLOY_SMELTER.get(),
			AMBlocks.BASIC_ALLOY_SMELTER.get(),
			AMBlocks.ADVANCED_ALLOY_SMELTER.get(),
			AMBlocks.ELITE_ALLOY_SMELTER.get(),
			
			AMBlocks.PRIMITIVE_TRITURATOR.get(),
			AMBlocks.BASIC_TRITURATOR.get(),
			AMBlocks.ADVANCED_TRITURATOR.get(),
			AMBlocks.ELITE_TRITURATOR.get(),
			
			AMBlocks.PRIMITIVE_PRESSER.get(),
			AMBlocks.BASIC_PRESSER.get(),
			AMBlocks.ADVANCED_PRESSER.get(),
			AMBlocks.ELITE_PRESSER.get(),
			
			AMBlocks.PRIMITIVE_WIREMILL.get(),
			AMBlocks.BASIC_WIREMILL.get(),
			AMBlocks.ADVANCED_WIREMILL.get(),
			AMBlocks.ELITE_WIREMILL.get(),
			
			AMBlocks.PRIMITIVE_ELECTROLYZER.get(),
			AMBlocks.BASIC_ELECTROLYZER.get(),
			AMBlocks.ADVANCED_ELECTROLYZER.get(),
			AMBlocks.ELITE_ELECTROLYZER.get(),
			
			AMBlocks.PRIMITIVE_REFINERY.get(),
			AMBlocks.BASIC_REFINERY.get(),
			AMBlocks.ADVANCED_REFINERY.get(),
			AMBlocks.ELITE_REFINERY.get(),
			
			AMBlocks.PRIMITIVE_FLUID_MIXER.get(),
			AMBlocks.BASIC_FLUID_MIXER.get(),
			AMBlocks.ADVANCED_FLUID_MIXER.get(),
			AMBlocks.ELITE_FLUID_MIXER.get(),
			
			AMBlocks.PRIMITIVE_SOLIDIFIER.get(),
			AMBlocks.BASIC_SOLIDIFIER.get(),
			AMBlocks.ADVANCED_SOLIDIFIER.get(),
			AMBlocks.ELITE_SOLIDIFIER.get(),
			
			AMBlocks.PRIMITIVE_MELTER.get(),
			AMBlocks.BASIC_MELTER.get(),
			AMBlocks.ADVANCED_MELTER.get(),
			AMBlocks.ELITE_MELTER.get(),
			
			AMBlocks.PRIMITIVE_BUFFER.get(),
			AMBlocks.BASIC_BUFFER.get(),
			AMBlocks.ADVANCED_BUFFER.get(),
			AMBlocks.ELITE_BUFFER.get(),
			AMBlocks.CREATIVE_BUFFER.get(),
			
			AMBlocks.FLUID_EXTRACTOR.get(),
			AMBlocks.FLUID_INSERTER.get(),
			AMBlocks.BLOCK_BREAKER.get(),
			AMBlocks.BLOCK_PLACER.get(),
			
			AMBlocks.NUCLEAR_WARHEAD.get(),
			
			AMBlocks.PRIMITIVE_CAPACITOR.get(),
			AMBlocks.BASIC_CAPACITOR.get(),
			AMBlocks.ADVANCED_CAPACITOR.get(),
			AMBlocks.ELITE_CAPACITOR.get(),
			AMBlocks.CREATIVE_CAPACITOR.get(),
			
			AMBlocks.AIRLOCK
	));
	
	public final LootTableGenerator SLABS = register(new SlabLootTableGenerator(
			AMBlocks.ASTEROID_STONE_SLAB.get(),
			AMBlocks.SMOOTH_ASTEROID_STONE_SLAB.get(),
			AMBlocks.POLISHED_ASTEROID_STONE_SLAB.get(),
			AMBlocks.ASTEROID_STONE_BRICK_SLAB.get(),
			AMBlocks.METEOR_STONE_SLAB.get(),
			AMBlocks.SMOOTH_METEOR_STONE_SLAB.get(),
			AMBlocks.POLISHED_METEOR_STONE_SLAB.get(),
			AMBlocks.METEOR_STONE_BRICK_SLAB.get()
	));
	
	public final LootTableGenerator BLOCK = register(new DropSelfSetLootTableGenerator(MaterialItemType.BLOCK));
	public final LootTableGenerator ORE = register(new DropSelfSetLootTableGenerator(MaterialItemType.ORE));
	
	public final LootTableGenerator METEOR_ORE = register(new FortuneMultiOreSetLootTableGenerator(MaterialItemType.METEOR_ORE, MaterialItemType.METEOR_CLUSTER, new UniformLootTableRange(2)));
	
	public SetLootTableGenerator register(SetLootTableGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public OneTimeLootTableGenerator register(OneTimeLootTableGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public void generateLootTables(LootTableData lootTables) {
		AMMaterialSets.getMaterialSets().forEach((set) -> generateSetLootTables(lootTables, set));
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
