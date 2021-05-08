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

package com.github.mixinors.astromine.technologies.datagen;

import com.github.mixinors.astromine.datagen.generator.modelstate.ModelStateGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.onetime.GenericItemModelGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.onetime.HandheldItemModelGenerator;
import com.github.mixinors.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.mixinors.astromine.technologies.datagen.generators.modelstate.MachineModelStateGenerator;

public class AstromineModelStateGenerators extends AstromineModelStateGenerators {
	public final ModelStateGenerator PRIMITIVE_MACHINES = register(new MachineModelStateGenerator(
			AstromineBlocks.PRIMITIVE_ALLOY_SMELTER,
			AstromineBlocks.PRIMITIVE_ELECTRIC_FURNACE,
			AstromineBlocks.PRIMITIVE_ELECTROLYZER,
			AstromineBlocks.PRIMITIVE_REFINERY,
			AstromineBlocks.PRIMITIVE_FLUID_MIXER,
			AstromineBlocks.PRIMITIVE_LIQUID_GENERATOR,
			AstromineBlocks.PRIMITIVE_PRESSER,
			AstromineBlocks.PRIMITIVE_SOLID_GENERATOR,
			AstromineBlocks.PRIMITIVE_TRITURATOR,
			AstromineBlocks.PRIMITIVE_WIREMILL,
			AstromineBlocks.PRIMITIVE_SOLIDIFIER,
			AstromineBlocks.PRIMITIVE_MELTER,
			AstromineBlocks.PRIMITIVE_BUFFER,
			AstromineBlocks.PRIMITIVE_CAPACITOR,
			AstromineBlocks.PRIMITIVE_TANK
	));

	public final ModelStateGenerator BASIC_MACHINES = register(new MachineModelStateGenerator(
			AstromineBlocks.BASIC_ALLOY_SMELTER,
			AstromineBlocks.BASIC_ELECTRIC_FURNACE,
			AstromineBlocks.BASIC_ELECTROLYZER,
			AstromineBlocks.BASIC_REFINERY,
			AstromineBlocks.BASIC_FLUID_MIXER,
			AstromineBlocks.BASIC_LIQUID_GENERATOR,
			AstromineBlocks.BASIC_PRESSER,
			AstromineBlocks.BASIC_SOLID_GENERATOR,
			AstromineBlocks.BASIC_TRITURATOR,
			AstromineBlocks.BASIC_WIREMILL,
			AstromineBlocks.BASIC_SOLIDIFIER,
			AstromineBlocks.BASIC_MELTER,
			AstromineBlocks.BASIC_BUFFER,
			AstromineBlocks.BASIC_CAPACITOR,
			AstromineBlocks.BASIC_TANK
			));

	public final ModelStateGenerator ADVANCED_MACHINES = register(new MachineModelStateGenerator(
			AstromineBlocks.ADVANCED_ALLOY_SMELTER,
			AstromineBlocks.ADVANCED_ELECTRIC_FURNACE,
			AstromineBlocks.ADVANCED_ELECTROLYZER,
			AstromineBlocks.ADVANCED_REFINERY,
			AstromineBlocks.ADVANCED_FLUID_MIXER,
			AstromineBlocks.ADVANCED_LIQUID_GENERATOR,
			AstromineBlocks.ADVANCED_PRESSER,
			AstromineBlocks.ADVANCED_SOLID_GENERATOR,
			AstromineBlocks.ADVANCED_TRITURATOR,
			AstromineBlocks.ADVANCED_WIREMILL,
			AstromineBlocks.ADVANCED_SOLIDIFIER,
			AstromineBlocks.ADVANCED_MELTER,
			AstromineBlocks.ADVANCED_BUFFER,
			AstromineBlocks.ADVANCED_CAPACITOR,
			AstromineBlocks.ADVANCED_TANK
	));

	public final ModelStateGenerator ELITE_MACHINES = register(new MachineModelStateGenerator(
			AstromineBlocks.ELITE_ALLOY_SMELTER,
			AstromineBlocks.ELITE_ELECTRIC_FURNACE,
			AstromineBlocks.ELITE_ELECTROLYZER,
			AstromineBlocks.ELITE_REFINERY,
			AstromineBlocks.ELITE_FLUID_MIXER,
			AstromineBlocks.ELITE_LIQUID_GENERATOR,
			AstromineBlocks.ELITE_PRESSER,
			AstromineBlocks.ELITE_SOLID_GENERATOR,
			AstromineBlocks.ELITE_TRITURATOR,
			AstromineBlocks.ELITE_WIREMILL,
			AstromineBlocks.ELITE_SOLIDIFIER,
			AstromineBlocks.ELITE_MELTER,
			AstromineBlocks.ELITE_BUFFER,
			AstromineBlocks.ELITE_CAPACITOR,
			AstromineBlocks.ELITE_TANK
	));

	public final ModelStateGenerator CREATIVE_MACHINES = register(new MachineModelStateGenerator(
			AstromineBlocks.CREATIVE_BUFFER,
			AstromineBlocks.CREATIVE_CAPACITOR,
			AstromineBlocks.CREATIVE_TANK
	));

	public final ModelStateGenerator MACHINE_CHASSIS = register(new GenericItemModelGenerator(
			AstromineItems.MACHINE_CHASSIS
	));
	
	public final ModelStateGenerator PLATINGS = register(new GenericItemModelGenerator(
			AstromineItems.PRIMITIVE_PLATING,
			AstromineItems.BASIC_PLATING,
			AstromineItems.ADVANCED_PLATING,
			AstromineItems.ELITE_PLATING
	));

	public final ModelStateGenerator UPGRADE_KIT = register(new GenericItemModelGenerator(
			AstromineItems.BASIC_MACHINE_UPGRADE_KIT,
			AstromineItems.ADVANCED_MACHINE_UPGRADE_KIT,
			AstromineItems.ELITE_MACHINE_UPGRADE_KIT
	));

	public final ModelStateGenerator CANISTERS = register(new GenericItemModelGenerator(
			AstromineItems.LARGE_PORTABLE_TANK,
			AstromineItems.PORTABLE_TANK
	));

	public final ModelStateGenerator CIRCUITS = register(new GenericItemModelGenerator(
			AstromineItems.PRIMITIVE_CIRCUIT,
			AstromineItems.BASIC_CIRCUIT,
			AstromineItems.ADVANCED_CIRCUIT,
			AstromineItems.ELITE_CIRCUIT
	));

	public final ModelStateGenerator BATTERIES = register(new GenericItemModelGenerator(
			AstromineItems.PRIMITIVE_BATTERY,
			AstromineItems.BASIC_BATTERY,
			AstromineItems.ADVANCED_BATTERY,
			AstromineItems.ELITE_BATTERY,
			AstromineItems.CREATIVE_BATTERY
	));

	public final ModelStateGenerator BATTERY_PACKS = register(new GenericItemModelGenerator(
			AstromineItems.PRIMITIVE_BATTERY_PACK,
			AstromineItems.BASIC_BATTERY_PACK,
			AstromineItems.ADVANCED_BATTERY_PACK,
			AstromineItems.ELITE_BATTERY_PACK,
			AstromineItems.CREATIVE_BATTERY_PACK
	));

	public final ModelStateGenerator DRILLS = register(new HandheldItemModelGenerator(
			AstromineItems.PRIMITIVE_DRILL,
			AstromineItems.BASIC_DRILL,
			AstromineItems.ADVANCED_DRILL,
			AstromineItems.ELITE_DRILL
	));

	public final ModelStateGenerator DRILL_HEADS = register(new HandheldItemModelGenerator(
			AstromineItems.PRIMITIVE_DRILL_BASE,
			AstromineItems.BASIC_DRILL_BASE,
			AstromineItems.ADVANCED_DRILL_BASE,
			AstromineItems.ELITE_DRILL_BASE
	));

	public final ModelStateGenerator DRILL_HEAD = register(new HandheldItemModelGenerator(
			AstromineItems.DRILL_HEAD
	));

	public final ModelStateGenerator HOLOGRAPHIC_CONNECTOR = register(new HandheldItemModelGenerator(
			AstromineItems.HOLOGRAPHIC_CONNECTOR
	));
}
