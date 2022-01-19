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

package com.github.mixinors.astromine.common.block.machine;

import com.github.mixinors.astromine.common.block.base.HorizontalFacingTieredBlockWithEntity;
import com.github.mixinors.astromine.common.block.entity.machine.MelterBlockEntity;
import com.github.mixinors.astromine.common.util.data.redstone.ComparatorMode;
import com.github.mixinors.astromine.common.screenhandler.machine.MelterScreenHandler;
import com.github.mixinors.astromine.common.util.data.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class MelterBlock extends HorizontalFacingTieredBlockWithEntity {
	public MelterBlock(Settings settings) {
		super(settings);
	}

	@Override
	public SavedData getSavedDataForDroppedItem() {
		return ITEM_AND_FLUID_MACHINE;
	}

	@Override
	protected ComparatorMode getComparatorMode() {
		return ComparatorMode.FLUIDS;
	}

	@Override
	public Block getForTier(MachineTier tier) {
		return switch (tier) {
			case PRIMITIVE -> AMBlocks.PRIMITIVE_MELTER.get();
			case BASIC -> AMBlocks.BASIC_MELTER.get();
			case ADVANCED -> AMBlocks.ADVANCED_MELTER.get();
			case ELITE -> AMBlocks.ELITE_MELTER.get();
			case CREATIVE -> null;
		};
	}

	@Override
	public boolean hasScreenHandler() {
		return true;
	}

	@Override
	public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new MelterScreenHandler(syncId, playerInventory.player, pos);
	}

	@Override
	public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {
		buffer.writeBlockPos(pos);
	}

	public static class Primitive extends MelterBlock {
		public Primitive(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new MelterBlockEntity.Primitive(pos, state);
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends MelterBlock {
		public Basic(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new MelterBlockEntity.Basic(pos, state);
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends MelterBlock {
		public Advanced(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new MelterBlockEntity.Advanced(pos, state);
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends MelterBlock {
		public Elite(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new MelterBlockEntity.Elite(pos, state);
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.ELITE;
		}
	}
}
