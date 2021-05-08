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
			AMBlocks.ALTAR,
			AMBlocks.ALTAR_PEDESTAL,
			AMBlocks.ASTEROID_STONE,
			AMBlocks.ASTEROID_STONE_STAIRS,
			AMBlocks.ASTEROID_STONE_WALL,
			AMBlocks.SMOOTH_ASTEROID_STONE,
			AMBlocks.SMOOTH_ASTEROID_STONE_STAIRS,
			AMBlocks.SMOOTH_ASTEROID_STONE_WALL,
			AMBlocks.POLISHED_ASTEROID_STONE,
			AMBlocks.POLISHED_ASTEROID_STONE_STAIRS,
			AMBlocks.ASTEROID_STONE_BRICKS,
			AMBlocks.ASTEROID_STONE_BRICK_STAIRS,
			AMBlocks.ASTEROID_STONE_BRICK_WALL,
			AMBlocks.BLAZING_ASTEROID_STONE,
			AMBlocks.SPACE_SLIME_BLOCK,
			AMBlocks.METEOR_STONE,
			AMBlocks.METEOR_STONE_STAIRS,
			AMBlocks.METEOR_STONE_WALL,
			AMBlocks.SMOOTH_METEOR_STONE,
			AMBlocks.SMOOTH_METEOR_STONE_STAIRS,
			AMBlocks.SMOOTH_METEOR_STONE_WALL,
			AMBlocks.POLISHED_METEOR_STONE,
			AMBlocks.POLISHED_METEOR_STONE_STAIRS,
			AMBlocks.METEOR_STONE_BRICKS,
			AMBlocks.METEOR_STONE_BRICK_STAIRS,
			AMBlocks.METEOR_STONE_BRICK_WALL,
			AMBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR,
			AMBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK,
			
			AMBlocks.VENT,
			
			AMBlocks.PRIMITIVE_TANK,
			AMBlocks.BASIC_TANK,
			AMBlocks.ADVANCED_TANK,
			AMBlocks.ELITE_TANK,
			AMBlocks.CREATIVE_TANK,
			
			AMBlocks.PRIMITIVE_SOLID_GENERATOR,
			AMBlocks.BASIC_SOLID_GENERATOR,
			AMBlocks.ADVANCED_SOLID_GENERATOR,
			AMBlocks.ELITE_SOLID_GENERATOR,
			
			AMBlocks.PRIMITIVE_LIQUID_GENERATOR,
			AMBlocks.BASIC_LIQUID_GENERATOR,
			AMBlocks.ADVANCED_LIQUID_GENERATOR,
			AMBlocks.ELITE_LIQUID_GENERATOR,
			
			AMBlocks.PRIMITIVE_ELECTRIC_FURNACE,
			AMBlocks.BASIC_ELECTRIC_FURNACE,
			AMBlocks.ADVANCED_ELECTRIC_FURNACE,
			AMBlocks.ELITE_ELECTRIC_FURNACE,
			
			AMBlocks.PRIMITIVE_ALLOY_SMELTER,
			AMBlocks.BASIC_ALLOY_SMELTER,
			AMBlocks.ADVANCED_ALLOY_SMELTER,
			AMBlocks.ELITE_ALLOY_SMELTER,
			
			AMBlocks.PRIMITIVE_TRITURATOR,
			AMBlocks.BASIC_TRITURATOR,
			AMBlocks.ADVANCED_TRITURATOR,
			AMBlocks.ELITE_TRITURATOR,
			
			AMBlocks.PRIMITIVE_PRESSER,
			AMBlocks.BASIC_PRESSER,
			AMBlocks.ADVANCED_PRESSER,
			AMBlocks.ELITE_PRESSER,
			
			AMBlocks.PRIMITIVE_WIREMILL,
			AMBlocks.BASIC_WIREMILL,
			AMBlocks.ADVANCED_WIREMILL,
			AMBlocks.ELITE_WIREMILL,
			
			AMBlocks.PRIMITIVE_ELECTROLYZER,
			AMBlocks.BASIC_ELECTROLYZER,
			AMBlocks.ADVANCED_ELECTROLYZER,
			AMBlocks.ELITE_ELECTROLYZER,
			
			AMBlocks.PRIMITIVE_REFINERY,
			AMBlocks.BASIC_REFINERY,
			AMBlocks.ADVANCED_REFINERY,
			AMBlocks.ELITE_REFINERY,
			
			AMBlocks.PRIMITIVE_FLUID_MIXER,
			AMBlocks.BASIC_FLUID_MIXER,
			AMBlocks.ADVANCED_FLUID_MIXER,
			AMBlocks.ELITE_FLUID_MIXER,
			
			AMBlocks.PRIMITIVE_SOLIDIFIER,
			AMBlocks.BASIC_SOLIDIFIER,
			AMBlocks.ADVANCED_SOLIDIFIER,
			AMBlocks.ELITE_SOLIDIFIER,
			
			AMBlocks.PRIMITIVE_MELTER,
			AMBlocks.BASIC_MELTER,
			AMBlocks.ADVANCED_MELTER,
			AMBlocks.ELITE_MELTER,
			
			AMBlocks.PRIMITIVE_BUFFER,
			AMBlocks.BASIC_BUFFER,
			AMBlocks.ADVANCED_BUFFER,
			AMBlocks.ELITE_BUFFER,
			AMBlocks.CREATIVE_BUFFER,
			
			AMBlocks.FLUID_EXTRACTOR,
			AMBlocks.FLUID_INSERTER,
			AMBlocks.BLOCK_BREAKER,
			AMBlocks.BLOCK_PLACER,
			
			AMBlocks.NUCLEAR_WARHEAD,
			
			AMBlocks.PRIMITIVE_CAPACITOR,
			AMBlocks.BASIC_CAPACITOR,
			AMBlocks.ADVANCED_CAPACITOR,
			AMBlocks.ELITE_CAPACITOR,
			AMBlocks.CREATIVE_CAPACITOR,
			
			AMBlocks.AIRLOCK
	));
	
	public final LootTableGenerator SLABS = register(new SlabLootTableGenerator(
			AMBlocks.ASTEROID_STONE_SLAB,
			AMBlocks.SMOOTH_ASTEROID_STONE_SLAB,
			AMBlocks.POLISHED_ASTEROID_STONE_SLAB,
			AMBlocks.ASTEROID_STONE_BRICK_SLAB,
			AMBlocks.METEOR_STONE_SLAB,
			AMBlocks.SMOOTH_METEOR_STONE_SLAB,
			AMBlocks.POLISHED_METEOR_STONE_SLAB,
			AMBlocks.METEOR_STONE_BRICK_SLAB
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
