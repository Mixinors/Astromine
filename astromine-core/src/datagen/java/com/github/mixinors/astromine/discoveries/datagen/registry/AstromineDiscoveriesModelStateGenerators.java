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

import com.github.mixinors.astromine.AstromineCommon;
import com.github.mixinors.astromine.datagen.generator.modelstate.ModelStateGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.onetime.GenericBlockModelStateGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.onetime.GenericItemModelGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.onetime.SimpleBlockItemModelGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.onetime.SimpleBlockItemModelStateGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.set.ColumnBlockSetModelStateGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.set.GenericBlockSetModelStateGenerator;
import com.github.mixinors.astromine.datagen.generator.modelstate.set.GenericItemSetModelGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.mixinors.astromine.registry.AstromineDiscoveriesBlocks;
import com.github.mixinors.astromine.registry.AstromineDiscoveriesItems;

public class AstromineDiscoveriesModelStateGenerators extends AstromineModelStateGenerators {
	public final ModelStateGenerator ASTEROID_ORE = register(new ColumnBlockSetModelStateGenerator(MaterialItemType.ASTEROID_ORE, AstromineCommon.identifier("block/asteroid_stone")));

	public final ModelStateGenerator ASTEROID_CLUSTER = register(new GenericItemSetModelGenerator(MaterialItemType.ASTEROID_CLUSTER));

	public final ModelStateGenerator MOON_ORE = register(new GenericBlockSetModelStateGenerator(MaterialItemType.MOON_ORE));

	public final ModelStateGenerator STANDARD_ITEMS = register(new GenericItemModelGenerator(
			AstromineDiscoveriesItems.SPACE_SUIT_HELMET,
			AstromineDiscoveriesItems.SPACE_SUIT_CHESTPLATE,
			AstromineDiscoveriesItems.SPACE_SUIT_LEGGINGS,
			AstromineDiscoveriesItems.SPACE_SUIT_BOOTS,
			AstromineDiscoveriesItems.SPACE_SLIME_BALL,
			AstromineDiscoveriesItems.SPACE_SLIME_SPAWN_EGG,
			AstromineDiscoveriesItems.PRIMITIVE_ROCKET_BOOSTER,
			AstromineDiscoveriesItems.PRIMITIVE_ROCKET_FUEL_TANK,
			AstromineDiscoveriesItems.PRIMITIVE_ROCKET_HULL,
			AstromineDiscoveriesItems.PRIMITIVE_ROCKET_PLATING
	));

	public final ModelStateGenerator STANDARD_BLOCKS = register(new GenericBlockModelStateGenerator(
			AstromineDiscoveriesBlocks.ASTEROID_STONE,
			AstromineDiscoveriesBlocks.SMOOTH_ASTEROID_STONE,
			AstromineDiscoveriesBlocks.POLISHED_ASTEROID_STONE,
			AstromineDiscoveriesBlocks.ASTEROID_STONE_BRICKS,
			AstromineDiscoveriesBlocks.BLAZING_ASTEROID_STONE,
			AstromineDiscoveriesBlocks.MOON_STONE,
			AstromineDiscoveriesBlocks.POLISHED_MARTIAN_STONE,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_BRICKS,
			AstromineDiscoveriesBlocks.VULCAN_STONE,
			AstromineDiscoveriesBlocks.SMOOTH_VULCAN_STONE,
			AstromineDiscoveriesBlocks.POLISHED_VULCAN_STONE,
			AstromineDiscoveriesBlocks.VULCAN_STONE_BRICKS
	));

	public final ModelStateGenerator CUSTOM_MODEL_BLOCKS = register(new SimpleBlockItemModelStateGenerator(
			AstromineDiscoveriesBlocks.ALTAR,
			AstromineDiscoveriesBlocks.ALTAR_PEDESTAL,
			AstromineDiscoveriesBlocks.MARTIAN_SOIL,
			AstromineDiscoveriesBlocks.MARTIAN_STONE,
			AstromineDiscoveriesBlocks.SMOOTH_MARTIAN_STONE,
			AstromineDiscoveriesBlocks.SPACE_SLIME_BLOCK
	));

	public final ModelStateGenerator CUSTOM_MODEL_AND_STATE_BLOCKS = register(new SimpleBlockItemModelGenerator(
			AstromineDiscoveriesBlocks.ASTEROID_STONE_SLAB,
			AstromineDiscoveriesBlocks.ASTEROID_STONE_STAIRS,
			AstromineDiscoveriesBlocks.SMOOTH_ASTEROID_STONE_SLAB,
			AstromineDiscoveriesBlocks.SMOOTH_ASTEROID_STONE_STAIRS,
			AstromineDiscoveriesBlocks.POLISHED_ASTEROID_STONE_SLAB,
			AstromineDiscoveriesBlocks.POLISHED_ASTEROID_STONE_STAIRS,
			AstromineDiscoveriesBlocks.ASTEROID_STONE_BRICK_SLAB,
			AstromineDiscoveriesBlocks.ASTEROID_STONE_BRICK_STAIRS,
			AstromineDiscoveriesBlocks.MOON_STONE_SLAB,
			AstromineDiscoveriesBlocks.MOON_STONE_STAIRS,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.SMOOTH_MARTIAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.SMOOTH_MARTIAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.POLISHED_MARTIAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.POLISHED_MARTIAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_BRICK_SLAB,
			AstromineDiscoveriesBlocks.MARTIAN_STONE_BRICK_STAIRS,
			AstromineDiscoveriesBlocks.VULCAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.VULCAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.SMOOTH_VULCAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.SMOOTH_VULCAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.POLISHED_VULCAN_STONE_SLAB,
			AstromineDiscoveriesBlocks.POLISHED_VULCAN_STONE_STAIRS,
			AstromineDiscoveriesBlocks.VULCAN_STONE_BRICK_SLAB,
			AstromineDiscoveriesBlocks.VULCAN_STONE_BRICK_STAIRS
	));

	public static void initialize() {

	}
}
