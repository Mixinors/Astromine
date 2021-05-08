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

package com.github.mixinors.astromine.discoveries.datagen.registry;

import com.github.mixinors.astromine.datagen.generator.loottable.LootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.onetime.DropSelfLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.onetime.SlabLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.set.DropSelfSetLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.set.FortuneOreSetLootTableGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.registry.AstromineLootTableGenerators;

public class AstromineLootTableGenerators extends AstromineLootTableGenerators {
	public final LootTableGenerator ASTEROID_ORE = register(new FortuneOreSetLootTableGenerator(MaterialItemType.ASTEROID_ORE, MaterialItemType.ASTEROID_CLUSTER));
	public final LootTableGenerator MOON_ORE = register(new DropSelfSetLootTableGenerator(MaterialItemType.MOON_ORE));

	public final LootTableGenerator DROP_SELF = register(new DropSelfLootTableGenerator(
			AstromineBlocks.ALTAR,
			AstromineBlocks.ALTAR_PEDESTAL,
			AstromineBlocks.ASTEROID_STONE,
			AstromineBlocks.ASTEROID_STONE_STAIRS,
			AstromineBlocks.ASTEROID_STONE_WALL,
			AstromineBlocks.SMOOTH_ASTEROID_STONE,
			AstromineBlocks.SMOOTH_ASTEROID_STONE_STAIRS,
			AstromineBlocks.SMOOTH_ASTEROID_STONE_WALL,
			AstromineBlocks.POLISHED_ASTEROID_STONE,
			AstromineBlocks.POLISHED_ASTEROID_STONE_STAIRS,
			AstromineBlocks.ASTEROID_STONE_BRICKS,
			AstromineBlocks.ASTEROID_STONE_BRICK_STAIRS,
			AstromineBlocks.ASTEROID_STONE_BRICK_WALL,
			AstromineBlocks.BLAZING_ASTEROID_STONE,
			AstromineBlocks.MOON_STONE,
			AstromineBlocks.MOON_STONE_STAIRS,
			AstromineBlocks.MOON_STONE_WALL,
			AstromineBlocks.MARTIAN_SOIL,
			AstromineBlocks.MARTIAN_STONE,
			AstromineBlocks.MARTIAN_STONE_STAIRS,
			AstromineBlocks.MARTIAN_STONE_WALL,
			AstromineBlocks.SMOOTH_MARTIAN_STONE,
			AstromineBlocks.SMOOTH_MARTIAN_STONE_STAIRS,
			AstromineBlocks.SMOOTH_MARTIAN_STONE_WALL,
			AstromineBlocks.POLISHED_MARTIAN_STONE,
			AstromineBlocks.POLISHED_MARTIAN_STONE_STAIRS,
			AstromineBlocks.MARTIAN_STONE_BRICKS,
			AstromineBlocks.MARTIAN_STONE_BRICK_STAIRS,
			AstromineBlocks.MARTIAN_STONE_BRICK_WALL,
			AstromineBlocks.VULCAN_STONE,
			AstromineBlocks.VULCAN_STONE_STAIRS,
			AstromineBlocks.VULCAN_STONE_WALL,
			AstromineBlocks.SMOOTH_VULCAN_STONE,
			AstromineBlocks.SMOOTH_VULCAN_STONE_STAIRS,
			AstromineBlocks.SMOOTH_VULCAN_STONE_WALL,
			AstromineBlocks.POLISHED_VULCAN_STONE,
			AstromineBlocks.POLISHED_VULCAN_STONE_STAIRS,
			AstromineBlocks.VULCAN_STONE_BRICKS,
			AstromineBlocks.VULCAN_STONE_BRICK_STAIRS,
			AstromineBlocks.VULCAN_STONE_BRICK_WALL,
			AstromineBlocks.SPACE_SLIME_BLOCK
	));

	public final LootTableGenerator SLABS = register(new SlabLootTableGenerator(
			AstromineBlocks.ASTEROID_STONE_SLAB,
			AstromineBlocks.SMOOTH_ASTEROID_STONE_SLAB,
			AstromineBlocks.POLISHED_ASTEROID_STONE_SLAB,
			AstromineBlocks.ASTEROID_STONE_BRICK_SLAB,
			AstromineBlocks.MOON_STONE_SLAB,
			AstromineBlocks.MARTIAN_STONE_SLAB,
			AstromineBlocks.SMOOTH_MARTIAN_STONE_SLAB,
			AstromineBlocks.POLISHED_MARTIAN_STONE_SLAB,
			AstromineBlocks.MARTIAN_STONE_BRICK_SLAB,
			AstromineBlocks.VULCAN_STONE_SLAB,
			AstromineBlocks.SMOOTH_VULCAN_STONE_SLAB,
			AstromineBlocks.POLISHED_VULCAN_STONE_SLAB,
			AstromineBlocks.VULCAN_STONE_BRICK_SLAB
	));

	public static void initialize() {

	}
}
