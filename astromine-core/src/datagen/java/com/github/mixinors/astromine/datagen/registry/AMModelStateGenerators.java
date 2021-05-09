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
import com.github.mixinors.astromine.datagen.generator.modelstate.ModelStateGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.onetime.*;
import com.github.mixinors.astromine.datagen.generator.modelstate.set.*;
import com.github.mixinors.astromine.datagen.generator.modelstate.MachineModelStateGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.material.MaterialSet;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMItems;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

import java.util.ArrayList;
import java.util.List;

public class AMModelStateGenerators {
	private final List<SetModelStateGenerator> SET_GENERATORS = new ArrayList<>();
	private final List<OneTimeModelStateGenerator> ONE_TIME_GENERATORS = new ArrayList<>();
	
	public final ModelStateGenerator ASTEROID_ORE = register(new ColumnBlockSetModelStateGenerator(MaterialItemType.ASTEROID_ORE, AMCommon.id("block/asteroid_stone")));
	
	public final ModelStateGenerator ASTEROID_CLUSTER = register(new GenericItemSetModelGenerator(MaterialItemType.ASTEROID_CLUSTER));
	
	
	public final ModelStateGenerator STANDARD_ITEMS = register(new GenericItemModelGenerator(
			AMItems.SPACE_SUIT_HELMET,
			AMItems.SPACE_SUIT_CHESTPLATE,
			AMItems.SPACE_SUIT_LEGGINGS,
			AMItems.SPACE_SUIT_BOOTS,
			AMItems.SPACE_SLIME_BALL,
			AMItems.SPACE_SLIME_SPAWN_EGG,
			AMItems.PRIMITIVE_ROCKET_BOOSTER,
			AMItems.PRIMITIVE_ROCKET_FUEL_TANK,
			AMItems.PRIMITIVE_ROCKET_HULL,
			AMItems.PRIMITIVE_ROCKET_PLATING
	));
	
	public final ModelStateGenerator STANDARD_BLOCKS = register(new GenericBlockModelStateGenerator(
			AMBlocks.ASTEROID_STONE,
			AMBlocks.SMOOTH_ASTEROID_STONE,
			AMBlocks.POLISHED_ASTEROID_STONE,
			AMBlocks.ASTEROID_STONE_BRICKS,
			AMBlocks.BLAZING_ASTEROID_STONE,
			AMBlocks.METEOR_STONE,
			AMBlocks.SMOOTH_METEOR_STONE,
			AMBlocks.POLISHED_METEOR_STONE,
			AMBlocks.METEOR_STONE_BRICKS
	));
	
	public final ModelStateGenerator CUSTOM_MODEL_BLOCKS = register(new SimpleBlockItemModelStateGenerator(
			AMBlocks.ALTAR,
			AMBlocks.ALTAR_PEDESTAL,
			AMBlocks.SPACE_SLIME_BLOCK
	));
	
	public final ModelStateGenerator CUSTOM_MODEL_AND_STATE_BLOCKS = register(new SimpleBlockItemModelGenerator(
			AMBlocks.ASTEROID_STONE_SLAB,
			AMBlocks.ASTEROID_STONE_STAIRS,
			AMBlocks.SMOOTH_ASTEROID_STONE_SLAB,
			AMBlocks.SMOOTH_ASTEROID_STONE_STAIRS,
			AMBlocks.POLISHED_ASTEROID_STONE_SLAB,
			AMBlocks.POLISHED_ASTEROID_STONE_STAIRS,
			AMBlocks.ASTEROID_STONE_BRICK_SLAB,
			AMBlocks.ASTEROID_STONE_BRICK_STAIRS,
			AMBlocks.METEOR_STONE_SLAB,
			AMBlocks.METEOR_STONE_STAIRS,
			AMBlocks.SMOOTH_METEOR_STONE_SLAB,
			AMBlocks.SMOOTH_METEOR_STONE_STAIRS,
			AMBlocks.POLISHED_METEOR_STONE_SLAB,
			AMBlocks.POLISHED_METEOR_STONE_STAIRS,
			AMBlocks.METEOR_STONE_BRICK_SLAB,
			AMBlocks.METEOR_STONE_BRICK_STAIRS
	));
	
	public final ModelStateGenerator INGOT = register(new GenericItemSetModelGenerator(MaterialItemType.INGOT));
	public final ModelStateGenerator GEM = register(new GenericItemSetModelGenerator(MaterialItemType.GEM));
	public final ModelStateGenerator MISC_RESOURCE = register(new GenericItemSetModelGenerator(MaterialItemType.MISC_RESOURCE));
	
	public final ModelStateGenerator NUGGET = register(new GenericItemSetModelGenerator(MaterialItemType.NUGGET));
	public final ModelStateGenerator FRAGMENT = register(new GenericItemSetModelGenerator(MaterialItemType.FRAGMENT));
	
	public final ModelStateGenerator BLOCK = register(new GenericBlockSetModelStateGenerator(MaterialItemType.BLOCK));
	public final ModelStateGenerator ORE = register(new GenericBlockSetModelStateGenerator(MaterialItemType.ORE));
	
	public final ModelStateGenerator METEOR_ORE = register(new ColumnBlockSetModelStateGenerator(MaterialItemType.METEOR_ORE, AMCommon.id("block/meteor_stone")));
	
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
	
	public final ModelStateGenerator APPLE = register(new GenericItemSetModelGenerator(MaterialItemType.APPLE));
	
	public final ModelStateGenerator BLADES = register(new HandheldItemModelGenerator(AMItems.BLADES));
	
	public final ModelStateGenerator BIOFUEL = register(new GenericItemModelGenerator(AMItems.BIOFUEL));
	
	public final ModelStateGenerator PRIMITIVE_MACHINES = register(new MachineModelStateGenerator(
			AMBlocks.PRIMITIVE_ALLOY_SMELTER,
			AMBlocks.PRIMITIVE_ELECTRIC_FURNACE,
			AMBlocks.PRIMITIVE_ELECTROLYZER,
			AMBlocks.PRIMITIVE_REFINERY,
			AMBlocks.PRIMITIVE_FLUID_MIXER,
			AMBlocks.PRIMITIVE_LIQUID_GENERATOR,
			AMBlocks.PRIMITIVE_PRESSER,
			AMBlocks.PRIMITIVE_SOLID_GENERATOR,
			AMBlocks.PRIMITIVE_TRITURATOR,
			AMBlocks.PRIMITIVE_WIREMILL,
			AMBlocks.PRIMITIVE_SOLIDIFIER,
			AMBlocks.PRIMITIVE_MELTER,
			AMBlocks.PRIMITIVE_BUFFER,
			AMBlocks.PRIMITIVE_CAPACITOR,
			AMBlocks.PRIMITIVE_TANK
	));
	
	public final ModelStateGenerator BASIC_MACHINES = register(new MachineModelStateGenerator(
			AMBlocks.BASIC_ALLOY_SMELTER,
			AMBlocks.BASIC_ELECTRIC_FURNACE,
			AMBlocks.BASIC_ELECTROLYZER,
			AMBlocks.BASIC_REFINERY,
			AMBlocks.BASIC_FLUID_MIXER,
			AMBlocks.BASIC_LIQUID_GENERATOR,
			AMBlocks.BASIC_PRESSER,
			AMBlocks.BASIC_SOLID_GENERATOR,
			AMBlocks.BASIC_TRITURATOR,
			AMBlocks.BASIC_WIREMILL,
			AMBlocks.BASIC_SOLIDIFIER,
			AMBlocks.BASIC_MELTER,
			AMBlocks.BASIC_BUFFER,
			AMBlocks.BASIC_CAPACITOR,
			AMBlocks.BASIC_TANK
	));
	
	public final ModelStateGenerator ADVANCED_MACHINES = register(new MachineModelStateGenerator(
			AMBlocks.ADVANCED_ALLOY_SMELTER,
			AMBlocks.ADVANCED_ELECTRIC_FURNACE,
			AMBlocks.ADVANCED_ELECTROLYZER,
			AMBlocks.ADVANCED_REFINERY,
			AMBlocks.ADVANCED_FLUID_MIXER,
			AMBlocks.ADVANCED_LIQUID_GENERATOR,
			AMBlocks.ADVANCED_PRESSER,
			AMBlocks.ADVANCED_SOLID_GENERATOR,
			AMBlocks.ADVANCED_TRITURATOR,
			AMBlocks.ADVANCED_WIREMILL,
			AMBlocks.ADVANCED_SOLIDIFIER,
			AMBlocks.ADVANCED_MELTER,
			AMBlocks.ADVANCED_BUFFER,
			AMBlocks.ADVANCED_CAPACITOR,
			AMBlocks.ADVANCED_TANK
	));
	
	public final ModelStateGenerator ELITE_MACHINES = register(new MachineModelStateGenerator(
			AMBlocks.ELITE_ALLOY_SMELTER,
			AMBlocks.ELITE_ELECTRIC_FURNACE,
			AMBlocks.ELITE_ELECTROLYZER,
			AMBlocks.ELITE_REFINERY,
			AMBlocks.ELITE_FLUID_MIXER,
			AMBlocks.ELITE_LIQUID_GENERATOR,
			AMBlocks.ELITE_PRESSER,
			AMBlocks.ELITE_SOLID_GENERATOR,
			AMBlocks.ELITE_TRITURATOR,
			AMBlocks.ELITE_WIREMILL,
			AMBlocks.ELITE_SOLIDIFIER,
			AMBlocks.ELITE_MELTER,
			AMBlocks.ELITE_BUFFER,
			AMBlocks.ELITE_CAPACITOR,
			AMBlocks.ELITE_TANK
	));
	
	public final ModelStateGenerator CREATIVE_MACHINES = register(new MachineModelStateGenerator(
			AMBlocks.CREATIVE_BUFFER,
			AMBlocks.CREATIVE_CAPACITOR,
			AMBlocks.CREATIVE_TANK
	));
	
	public final ModelStateGenerator MACHINE_CHASSIS = register(new GenericItemModelGenerator(
			AMItems.MACHINE_CHASSIS
	));
	
	public final ModelStateGenerator PLATINGS = register(new GenericItemModelGenerator(
			AMItems.PRIMITIVE_PLATING,
			AMItems.BASIC_PLATING,
			AMItems.ADVANCED_PLATING,
			AMItems.ELITE_PLATING
	));
	
	public final ModelStateGenerator UPGRADE_KIT = register(new GenericItemModelGenerator(
			AMItems.BASIC_MACHINE_UPGRADE_KIT,
			AMItems.ADVANCED_MACHINE_UPGRADE_KIT,
			AMItems.ELITE_MACHINE_UPGRADE_KIT
	));
	
	public final ModelStateGenerator CANISTERS = register(new GenericItemModelGenerator(
			AMItems.LARGE_PORTABLE_TANK,
			AMItems.PORTABLE_TANK
	));
	
	public final ModelStateGenerator CIRCUITS = register(new GenericItemModelGenerator(
			AMItems.PRIMITIVE_CIRCUIT,
			AMItems.BASIC_CIRCUIT,
			AMItems.ADVANCED_CIRCUIT,
			AMItems.ELITE_CIRCUIT
	));
	
	public final ModelStateGenerator BATTERIES = register(new GenericItemModelGenerator(
			AMItems.PRIMITIVE_BATTERY,
			AMItems.BASIC_BATTERY,
			AMItems.ADVANCED_BATTERY,
			AMItems.ELITE_BATTERY,
			AMItems.CREATIVE_BATTERY
	));
	
	public final ModelStateGenerator BATTERY_PACKS = register(new GenericItemModelGenerator(
			AMItems.PRIMITIVE_BATTERY_PACK,
			AMItems.BASIC_BATTERY_PACK,
			AMItems.ADVANCED_BATTERY_PACK,
			AMItems.ELITE_BATTERY_PACK,
			AMItems.CREATIVE_BATTERY_PACK
	));
	
	public final ModelStateGenerator DRILLS = register(new HandheldItemModelGenerator(
			AMItems.PRIMITIVE_DRILL,
			AMItems.BASIC_DRILL,
			AMItems.ADVANCED_DRILL,
			AMItems.ELITE_DRILL
	));
	
	public final ModelStateGenerator DRILL_HEADS = register(new HandheldItemModelGenerator(
			AMItems.PRIMITIVE_DRILL_BASE,
			AMItems.BASIC_DRILL_BASE,
			AMItems.ADVANCED_DRILL_BASE,
			AMItems.ELITE_DRILL_BASE
	));
	
	public final ModelStateGenerator DRILL_HEAD = register(new HandheldItemModelGenerator(
			AMItems.DRILL_HEAD
	));
	
	public final ModelStateGenerator HOLOGRAPHIC_CONNECTOR = register(new HandheldItemModelGenerator(
			AMItems.HOLOGRAPHIC_CONNECTOR
	));

	public SetModelStateGenerator register(SetModelStateGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public OneTimeModelStateGenerator register(OneTimeModelStateGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public void generateModelStates(ModelStateData modelStates) {
		AMMaterialSets.getMaterialSets().forEach((set) -> generateSetModelStates(modelStates, set));
		generateOneTimeRecipes(modelStates);
	}

	private void generateSetModelStates(ModelStateData modelStates, MaterialSet set) {
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(modelStates, set);
					AMCommon.LOGGER.info("Model/State generation of " + set.getName() + " succeeded, with generator " + generator.getGeneratorName() + ".");
				}
			} catch (Exception exception) {
				AMCommon.LOGGER.error("Model/State generation of " + set.getName() + " failed, with generator " + generator.getGeneratorName() + ".");
				AMCommon.LOGGER.error(exception.getMessage());
			}
		});
	}

	private void generateOneTimeRecipes(ModelStateData modelStates) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(modelStates));
	}
}
