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
			AMItems.SPACE_SUIT_HELMET.get(),
			AMItems.SPACE_SUIT_CHESTPLATE.get(),
			AMItems.SPACE_SUIT_LEGGINGS.get(),
			AMItems.SPACE_SUIT_BOOTS.get(),
			AMItems.SPACE_SLIME_BALL.get(),
			AMItems.SPACE_SLIME_SPAWN_EGG.get(),
			AMItems.PRIMITIVE_ROCKET_BOOSTER.get(),
			AMItems.PRIMITIVE_ROCKET_FUEL_TANK.get(),
			AMItems.PRIMITIVE_ROCKET_HULL.get(),
			AMItems.PRIMITIVE_ROCKET_PLATING.get()
	));
	
	public final ModelStateGenerator STANDARD_BLOCKS = register(new GenericBlockModelStateGenerator(
			AMBlocks.ASTEROID_STONE.get(),
			AMBlocks.SMOOTH_ASTEROID_STONE.get(),
			AMBlocks.POLISHED_ASTEROID_STONE.get(),
			AMBlocks.ASTEROID_STONE_BRICKS.get(),
			AMBlocks.BLAZING_ASTEROID_STONE.get(),
			AMBlocks.METEOR_STONE.get(),
			AMBlocks.SMOOTH_METEOR_STONE.get(),
			AMBlocks.POLISHED_METEOR_STONE.get(),
			AMBlocks.METEOR_STONE_BRICKS.get()
	));
	
	public final ModelStateGenerator CUSTOM_MODEL_BLOCKS = register(new SimpleBlockItemModelStateGenerator(
			AMBlocks.ALTAR.get(),
			AMBlocks.ALTAR_PEDESTAL.get(),
			AMBlocks.SPACE_SLIME_BLOCK.get()
	));
	
	public final ModelStateGenerator CUSTOM_MODEL_AND_STATE_BLOCKS = register(new SimpleBlockItemModelGenerator(
			AMBlocks.ASTEROID_STONE_SLAB.get(),
			AMBlocks.ASTEROID_STONE_STAIRS.get(),
			AMBlocks.SMOOTH_ASTEROID_STONE_SLAB.get(),
			AMBlocks.SMOOTH_ASTEROID_STONE_STAIRS.get(),
			AMBlocks.POLISHED_ASTEROID_STONE_SLAB.get(),
			AMBlocks.POLISHED_ASTEROID_STONE_STAIRS.get(),
			AMBlocks.ASTEROID_STONE_BRICK_SLAB.get(),
			AMBlocks.ASTEROID_STONE_BRICK_STAIRS.get(),
			AMBlocks.METEOR_STONE_SLAB.get(),
			AMBlocks.METEOR_STONE_STAIRS.get(),
			AMBlocks.SMOOTH_METEOR_STONE_SLAB.get(),
			AMBlocks.SMOOTH_METEOR_STONE_STAIRS.get(),
			AMBlocks.POLISHED_METEOR_STONE_SLAB.get(),
			AMBlocks.POLISHED_METEOR_STONE_STAIRS.get(),
			AMBlocks.METEOR_STONE_BRICK_SLAB.get(),
			AMBlocks.METEOR_STONE_BRICK_STAIRS.get()
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
	
	public final ModelStateGenerator BLADES = register(new HandheldItemModelGenerator(AMItems.BLADES.get()));
	
	public final ModelStateGenerator BIOFUEL = register(new GenericItemModelGenerator(AMItems.BIOFUEL.get()));
	
	public final ModelStateGenerator PRIMITIVE_MACHINES = register(new MachineModelStateGenerator(
			AMBlocks.PRIMITIVE_ALLOY_SMELTER.get(),
			AMBlocks.PRIMITIVE_ELECTRIC_FURNACE.get(),
			AMBlocks.PRIMITIVE_ELECTROLYZER.get(),
			AMBlocks.PRIMITIVE_REFINERY.get(),
			AMBlocks.PRIMITIVE_FLUID_MIXER.get(),
			AMBlocks.PRIMITIVE_LIQUID_GENERATOR.get(),
			AMBlocks.PRIMITIVE_PRESSER.get(),
			AMBlocks.PRIMITIVE_SOLID_GENERATOR.get(),
			AMBlocks.PRIMITIVE_TRITURATOR.get(),
			AMBlocks.PRIMITIVE_WIREMILL.get(),
			AMBlocks.PRIMITIVE_SOLIDIFIER.get(),
			AMBlocks.PRIMITIVE_MELTER.get(),
			AMBlocks.PRIMITIVE_BUFFER.get(),
			AMBlocks.PRIMITIVE_CAPACITOR.get(),
			AMBlocks.PRIMITIVE_TANK.get()
	));
	
	public final ModelStateGenerator BASIC_MACHINES = register(new MachineModelStateGenerator(
			AMBlocks.BASIC_ALLOY_SMELTER.get(),
			AMBlocks.BASIC_ELECTRIC_FURNACE.get(),
			AMBlocks.BASIC_ELECTROLYZER.get(),
			AMBlocks.BASIC_REFINERY.get(),
			AMBlocks.BASIC_FLUID_MIXER.get(),
			AMBlocks.BASIC_LIQUID_GENERATOR.get(),
			AMBlocks.BASIC_PRESSER.get(),
			AMBlocks.BASIC_SOLID_GENERATOR.get(),
			AMBlocks.BASIC_TRITURATOR.get(),
			AMBlocks.BASIC_WIREMILL.get(),
			AMBlocks.BASIC_SOLIDIFIER.get(),
			AMBlocks.BASIC_MELTER.get(),
			AMBlocks.BASIC_BUFFER.get(),
			AMBlocks.BASIC_CAPACITOR.get(),
			AMBlocks.BASIC_TANK.get()
	));
	
	public final ModelStateGenerator ADVANCED_MACHINES = register(new MachineModelStateGenerator(
			AMBlocks.ADVANCED_ALLOY_SMELTER.get(),
			AMBlocks.ADVANCED_ELECTRIC_FURNACE.get(),
			AMBlocks.ADVANCED_ELECTROLYZER.get(),
			AMBlocks.ADVANCED_REFINERY.get(),
			AMBlocks.ADVANCED_FLUID_MIXER.get(),
			AMBlocks.ADVANCED_LIQUID_GENERATOR.get(),
			AMBlocks.ADVANCED_PRESSER.get(),
			AMBlocks.ADVANCED_SOLID_GENERATOR.get(),
			AMBlocks.ADVANCED_TRITURATOR.get(),
			AMBlocks.ADVANCED_WIREMILL.get(),
			AMBlocks.ADVANCED_SOLIDIFIER.get(),
			AMBlocks.ADVANCED_MELTER.get(),
			AMBlocks.ADVANCED_BUFFER.get(),
			AMBlocks.ADVANCED_CAPACITOR.get(),
			AMBlocks.ADVANCED_TANK.get()
	));
	
	public final ModelStateGenerator ELITE_MACHINES = register(new MachineModelStateGenerator(
			AMBlocks.ELITE_ALLOY_SMELTER.get(),
			AMBlocks.ELITE_ELECTRIC_FURNACE.get(),
			AMBlocks.ELITE_ELECTROLYZER.get(),
			AMBlocks.ELITE_REFINERY.get(),
			AMBlocks.ELITE_FLUID_MIXER.get(),
			AMBlocks.ELITE_LIQUID_GENERATOR.get(),
			AMBlocks.ELITE_PRESSER.get(),
			AMBlocks.ELITE_SOLID_GENERATOR.get(),
			AMBlocks.ELITE_TRITURATOR.get(),
			AMBlocks.ELITE_WIREMILL.get(),
			AMBlocks.ELITE_SOLIDIFIER.get(),
			AMBlocks.ELITE_MELTER.get(),
			AMBlocks.ELITE_BUFFER.get(),
			AMBlocks.ELITE_CAPACITOR.get(),
			AMBlocks.ELITE_TANK.get()
	));
	
	public final ModelStateGenerator CREATIVE_MACHINES = register(new MachineModelStateGenerator(
			AMBlocks.CREATIVE_BUFFER.get(),
			AMBlocks.CREATIVE_CAPACITOR.get(),
			AMBlocks.CREATIVE_TANK.get()
	));
	
	public final ModelStateGenerator MACHINE_CHASSIS = register(new GenericItemModelGenerator(
			AMItems.MACHINE_CHASSIS.get()
	));
	
	public final ModelStateGenerator PLATINGS = register(new GenericItemModelGenerator(
			AMItems.PRIMITIVE_PLATING.get(),
			AMItems.BASIC_PLATING.get(),
			AMItems.ADVANCED_PLATING.get(),
			AMItems.ELITE_PLATING.get()
	));
	
	public final ModelStateGenerator UPGRADE_KIT = register(new GenericItemModelGenerator(
			AMItems.BASIC_MACHINE_UPGRADE_KIT.get(),
			AMItems.ADVANCED_MACHINE_UPGRADE_KIT.get(),
			AMItems.ELITE_MACHINE_UPGRADE_KIT.get()
	));
	
	public final ModelStateGenerator CANISTERS = register(new GenericItemModelGenerator(
			AMItems.LARGE_PORTABLE_TANK.get(),
			AMItems.PORTABLE_TANK.get()
	));
	
	public final ModelStateGenerator CIRCUITS = register(new GenericItemModelGenerator(
			AMItems.PRIMITIVE_CIRCUIT.get(),
			AMItems.BASIC_CIRCUIT.get(),
			AMItems.ADVANCED_CIRCUIT.get(),
			AMItems.ELITE_CIRCUIT.get()
	));
	
	public final ModelStateGenerator BATTERIES = register(new GenericItemModelGenerator(
			AMItems.PRIMITIVE_BATTERY.get(),
			AMItems.BASIC_BATTERY.get(),
			AMItems.ADVANCED_BATTERY.get(),
			AMItems.ELITE_BATTERY.get(),
			AMItems.CREATIVE_BATTERY.get()
	));
	
	public final ModelStateGenerator BATTERY_PACKS = register(new GenericItemModelGenerator(
			AMItems.PRIMITIVE_BATTERY_PACK.get(),
			AMItems.BASIC_BATTERY_PACK.get(),
			AMItems.ADVANCED_BATTERY_PACK.get(),
			AMItems.ELITE_BATTERY_PACK.get(),
			AMItems.CREATIVE_BATTERY_PACK.get()
	));
	
	public final ModelStateGenerator DRILLS = register(new HandheldItemModelGenerator(
			AMItems.PRIMITIVE_DRILL.get(),
			AMItems.BASIC_DRILL.get(),
			AMItems.ADVANCED_DRILL.get(),
			AMItems.ELITE_DRILL.get()
	));
	
	public final ModelStateGenerator DRILL_HEADS = register(new HandheldItemModelGenerator(
			AMItems.PRIMITIVE_DRILL_BASE.get(),
			AMItems.BASIC_DRILL_BASE.get(),
			AMItems.ADVANCED_DRILL_BASE.get(),
			AMItems.ELITE_DRILL_BASE.get()
	));
	
	public final ModelStateGenerator DRILL_HEAD = register(new HandheldItemModelGenerator(
			AMItems.DRILL_HEAD.get()
	));
	
	public final ModelStateGenerator HOLOGRAPHIC_CONNECTOR = register(new HandheldItemModelGenerator(
			AMItems.HOLOGRAPHIC_CONNECTOR.get()
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
