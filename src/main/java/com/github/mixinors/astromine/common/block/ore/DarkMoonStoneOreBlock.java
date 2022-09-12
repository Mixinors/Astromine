/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.block.ore;

import com.github.mixinors.astromine.common.block.ore.base.ExtendedOreBlock;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class DarkMoonStoneOreBlock extends ExtendedOreBlock {
	public DarkMoonStoneOreBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AMProperties.DYNAMIC);
		
		super.appendProperties(builder);
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(AMProperties.DYNAMIC, false);
	}
	
	@Override
	protected int getExperienceWhenMined(Random random) {
		if (this == AMBlocks.DARK_MOON_LUNUM_ORE.get()) {
			return MathHelper.nextInt(random,  1, 3);
		} else if (this == AMBlocks.DARK_MOON_COAL_ORE.get()) {
			return MathHelper.nextInt(random, 0, 2);
		} else if (this == AMBlocks.DARK_MOON_TIN_ORE.get() || this == AMBlocks.DARK_MOON_COPPER_ORE.get()) {
			return MathHelper.nextInt(random, 1, 2);
		} else if (this == AMBlocks.DARK_MOON_IRON_ORE.get()) {
			return MathHelper.nextInt(random, 1, 3);
		} else if (this == AMBlocks.DARK_MOON_GOLD_ORE.get()) {
			return MathHelper.nextInt(random, 2, 3);
		} else if (this == AMBlocks.DARK_MOON_DIAMOND_ORE.get() || this == AMBlocks.DARK_MOON_EMERALD_ORE.get()) {
			return MathHelper.nextInt(random, 3, 7);
		} else if (this == AMBlocks.DARK_MOON_LAPIS_ORE.get() || this == AMBlocks.DARK_MOON_REDSTONE_ORE.get()) {
			return MathHelper.nextInt(random, 2, 5);
		} else {
			return 0;
		}
	}
}
