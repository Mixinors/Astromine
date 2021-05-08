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

package com.github.mixinors.astromine.foundations.datagen.registry;

import net.minecraft.loot.UniformLootTableRange;

import com.github.mixinors.astromine.datagen.generator.loottable.LootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.onetime.DropSelfLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.onetime.SlabLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.set.DropSelfSetLootTableGenerator;
import com.github.mixinors.astromine.datagen.generator.loottable.set.FortuneMultiOreSetLootTableGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.registry.AstromineLootTableGenerators;

public class AstromineLootTableGenerators extends AstromineLootTableGenerators {
	public final LootTableGenerator BLOCK = register(new DropSelfSetLootTableGenerator(MaterialItemType.BLOCK));
	public final LootTableGenerator ORE = register(new DropSelfSetLootTableGenerator(MaterialItemType.ORE));

	public final LootTableGenerator DROP_SELF = register(new DropSelfLootTableGenerator(
			AstromineBlocks.METEOR_STONE,
			AstromineBlocks.METEOR_STONE_STAIRS,
			AstromineBlocks.METEOR_STONE_WALL,
			AstromineBlocks.SMOOTH_METEOR_STONE,
			AstromineBlocks.SMOOTH_METEOR_STONE_STAIRS,
			AstromineBlocks.SMOOTH_METEOR_STONE_WALL,
			AstromineBlocks.POLISHED_METEOR_STONE,
			AstromineBlocks.POLISHED_METEOR_STONE_STAIRS,
			AstromineBlocks.METEOR_STONE_BRICKS,
			AstromineBlocks.METEOR_STONE_BRICK_STAIRS,
			AstromineBlocks.METEOR_STONE_BRICK_WALL
	));

	public final LootTableGenerator SLABS = register(new SlabLootTableGenerator(
			AstromineBlocks.METEOR_STONE_SLAB,
			AstromineBlocks.SMOOTH_METEOR_STONE_SLAB,
			AstromineBlocks.POLISHED_METEOR_STONE_SLAB,
			AstromineBlocks.METEOR_STONE_BRICK_SLAB
	));

	public final LootTableGenerator METEOR_ORE = register(new FortuneMultiOreSetLootTableGenerator(MaterialItemType.METEOR_ORE, MaterialItemType.METEOR_CLUSTER, new UniformLootTableRange(2)));
}
