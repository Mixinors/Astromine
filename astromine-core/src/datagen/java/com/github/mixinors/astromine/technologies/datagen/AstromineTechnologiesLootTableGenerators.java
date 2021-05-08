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

import com.github.mixinors.astromine.datagen.generator.loottable.LootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.onetime.DropSelfLootTableGenerator;
import com.github.mixinors.astromine.datagen.registry.AstromineLootTableGenerators;

public class AstromineLootTableGenerators extends AstromineLootTableGenerators {
	public final LootTableGenerator DROP_SELF = register(new DropSelfLootTableGenerator(
			AstromineBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR,
			AstromineBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK,

			AstromineBlocks.VENT,

			AstromineBlocks.PRIMITIVE_TANK,
			AstromineBlocks.BASIC_TANK,
			AstromineBlocks.ADVANCED_TANK,
			AstromineBlocks.ELITE_TANK,
			AstromineBlocks.CREATIVE_TANK,

			AstromineBlocks.PRIMITIVE_SOLID_GENERATOR,
			AstromineBlocks.BASIC_SOLID_GENERATOR,
			AstromineBlocks.ADVANCED_SOLID_GENERATOR,
			AstromineBlocks.ELITE_SOLID_GENERATOR,

			AstromineBlocks.PRIMITIVE_LIQUID_GENERATOR,
			AstromineBlocks.BASIC_LIQUID_GENERATOR,
			AstromineBlocks.ADVANCED_LIQUID_GENERATOR,
			AstromineBlocks.ELITE_LIQUID_GENERATOR,

			AstromineBlocks.PRIMITIVE_ELECTRIC_FURNACE,
			AstromineBlocks.BASIC_ELECTRIC_FURNACE,
			AstromineBlocks.ADVANCED_ELECTRIC_FURNACE,
			AstromineBlocks.ELITE_ELECTRIC_FURNACE,

			AstromineBlocks.PRIMITIVE_ALLOY_SMELTER,
			AstromineBlocks.BASIC_ALLOY_SMELTER,
			AstromineBlocks.ADVANCED_ALLOY_SMELTER,
			AstromineBlocks.ELITE_ALLOY_SMELTER,

			AstromineBlocks.PRIMITIVE_TRITURATOR,
			AstromineBlocks.BASIC_TRITURATOR,
			AstromineBlocks.ADVANCED_TRITURATOR,
			AstromineBlocks.ELITE_TRITURATOR,

			AstromineBlocks.PRIMITIVE_PRESSER,
			AstromineBlocks.BASIC_PRESSER,
			AstromineBlocks.ADVANCED_PRESSER,
			AstromineBlocks.ELITE_PRESSER,

			AstromineBlocks.PRIMITIVE_WIREMILL,
			AstromineBlocks.BASIC_WIREMILL,
			AstromineBlocks.ADVANCED_WIREMILL,
			AstromineBlocks.ELITE_WIREMILL,

			AstromineBlocks.PRIMITIVE_ELECTROLYZER,
			AstromineBlocks.BASIC_ELECTROLYZER,
			AstromineBlocks.ADVANCED_ELECTROLYZER,
			AstromineBlocks.ELITE_ELECTROLYZER,

			AstromineBlocks.PRIMITIVE_REFINERY,
			AstromineBlocks.BASIC_REFINERY,
			AstromineBlocks.ADVANCED_REFINERY,
			AstromineBlocks.ELITE_REFINERY,

			AstromineBlocks.PRIMITIVE_FLUID_MIXER,
			AstromineBlocks.BASIC_FLUID_MIXER,
			AstromineBlocks.ADVANCED_FLUID_MIXER,
			AstromineBlocks.ELITE_FLUID_MIXER,

			AstromineBlocks.PRIMITIVE_SOLIDIFIER,
			AstromineBlocks.BASIC_SOLIDIFIER,
			AstromineBlocks.ADVANCED_SOLIDIFIER,
			AstromineBlocks.ELITE_SOLIDIFIER,

			AstromineBlocks.PRIMITIVE_MELTER,
			AstromineBlocks.BASIC_MELTER,
			AstromineBlocks.ADVANCED_MELTER,
			AstromineBlocks.ELITE_MELTER,

			AstromineBlocks.PRIMITIVE_BUFFER,
			AstromineBlocks.BASIC_BUFFER,
			AstromineBlocks.ADVANCED_BUFFER,
			AstromineBlocks.ELITE_BUFFER,
			AstromineBlocks.CREATIVE_BUFFER,

			AstromineBlocks.FLUID_EXTRACTOR,
			AstromineBlocks.FLUID_INSERTER,
			AstromineBlocks.BLOCK_BREAKER,
			AstromineBlocks.BLOCK_PLACER,

			AstromineBlocks.NUCLEAR_WARHEAD,

			AstromineBlocks.PRIMITIVE_CAPACITOR,
			AstromineBlocks.BASIC_CAPACITOR,
			AstromineBlocks.ADVANCED_CAPACITOR,
			AstromineBlocks.ELITE_CAPACITOR,
			AstromineBlocks.CREATIVE_CAPACITOR,

			AstromineBlocks.AIRLOCK));
}
