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

package com.github.mixinors.astromine.common.block.ore.base;

import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMCriteria;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ExtendedOreBlock extends Block {
	public ExtendedOreBlock(BlockBehaviour.Properties settings) {
		super(settings);
	}
	
	@Override
	public void spawnAfterBreak(BlockState state, ServerLevel world, BlockPos pos, ItemStack stack, boolean dropExperience) {
		super.spawnAfterBreak(state, world, pos, stack, dropExperience);
		
		var silkTouch = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
		
		if (EnchantmentHelper.getItemEnchantmentLevel(silkTouch, stack) == 0) {
			var i = getExperienceWhenMined(world.random);
			if (i > 0) {
				this.popExperience(world, pos, i);
			}
		}
		
	}
	
	protected int getExperienceWhenMined(RandomSource random) {
		if (this == AMBlocks.METEOR_METITE_ORE.get()) {
			return Mth.nextInt(random, 2, 3);
		} else {
			return 0;
		}
	}
	
	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		var destroyedState = super.playerWillDestroy(world, pos, state, player);
		
		if (this == AMBlocks.METEOR_METITE_ORE.get() && player instanceof ServerPlayer) {
			var stack = player.getItemInHand(InteractionHand.MAIN_HAND);
			
			if (!stack.isCorrectToolForDrops(state) && stack.isCorrectToolForDrops(Blocks.STONE.defaultBlockState())) {
				AMCriteria.UNDERESTIMATE_METITE.trigger((ServerPlayer) player);
			}
		}
		
		return destroyedState;
	}
}
