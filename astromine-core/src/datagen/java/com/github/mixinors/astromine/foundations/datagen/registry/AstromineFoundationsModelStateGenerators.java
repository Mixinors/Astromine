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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.generator.modelstate.ModelStateGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.onetime.GenericBlockModelStateGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.onetime.HandheldItemModelGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.onetime.SimpleBlockItemModelGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.set.ColumnBlockSetModelStateGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.set.GenericBlockSetModelStateGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.set.GenericItemSetModelGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.set.HandheldItemSetModelGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.registry.AMItems;

public class AstromineModelStateGenerators extends AstromineModelStateGenerators {
	public final ModelStateGenerator INGOT = register(new GenericItemSetModelGenerator(MaterialItemType.INGOT));
	public final ModelStateGenerator GEM = register(new GenericItemSetModelGenerator(MaterialItemType.GEM));
	public final ModelStateGenerator MISC_RESOURCE = register(new GenericItemSetModelGenerator(MaterialItemType.MISC_RESOURCE));

	public final ModelStateGenerator NUGGET = register(new GenericItemSetModelGenerator(MaterialItemType.NUGGET));
	public final ModelStateGenerator FRAGMENT = register(new GenericItemSetModelGenerator(MaterialItemType.FRAGMENT));

	public final ModelStateGenerator BLOCK = register(new GenericBlockSetModelStateGenerator(MaterialItemType.BLOCK));
	public final ModelStateGenerator ORE = register(new GenericBlockSetModelStateGenerator(MaterialItemType.ORE));

	public final ModelStateGenerator METEOR_ORE = register(new ColumnBlockSetModelStateGenerator(MaterialItemType.METEOR_ORE, AMCommon.identifier("block/meteor_stone")));

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

	public final ModelStateGenerator STANDARD_BLOCKS = register(new GenericBlockModelStateGenerator(
			AstromineBlocks.METEOR_STONE,
			AstromineBlocks.SMOOTH_METEOR_STONE,
			AstromineBlocks.POLISHED_METEOR_STONE,
			AstromineBlocks.METEOR_STONE_BRICKS
	));

	public final ModelStateGenerator CUSTOM_MODEL_AND_STATE_BLOCKS = register(new SimpleBlockItemModelGenerator(
			AstromineBlocks.METEOR_STONE_SLAB,
			AstromineBlocks.METEOR_STONE_STAIRS,
			AstromineBlocks.SMOOTH_METEOR_STONE_SLAB,
			AstromineBlocks.SMOOTH_METEOR_STONE_STAIRS,
			AstromineBlocks.POLISHED_METEOR_STONE_SLAB,
			AstromineBlocks.POLISHED_METEOR_STONE_STAIRS,
			AstromineBlocks.METEOR_STONE_BRICK_SLAB,
			AstromineBlocks.METEOR_STONE_BRICK_STAIRS
	));
	
	public final ModelStateGenerator BLADES = register(new HandheldItemModelGenerator(
			AMItems.BLADES
	));
}
