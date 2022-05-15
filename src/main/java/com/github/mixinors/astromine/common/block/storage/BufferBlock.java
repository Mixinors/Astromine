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

package com.github.mixinors.astromine.common.block.storage;

import com.github.mixinors.astromine.common.block.base.HorizontalFacingTieredBlockWithEntity;
import com.github.mixinors.astromine.common.block.entity.storage.BufferBlockEntity;
import com.github.mixinors.astromine.common.screenhandler.storage.BufferScreenHandler;
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

public abstract class BufferBlock extends HorizontalFacingTieredBlockWithEntity {
	public BufferBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public SavedData getSavedDataForDroppedItem() {
		return new SavedData(true, false, true, false);
	}
	
	@Override
	public boolean hasScreenHandler() {
		return true;
	}
	
	@Override
	public Block getForTier(MachineTier tier) {
		return switch (tier) {
			case PRIMITIVE -> AMBlocks.PRIMITIVE_BUFFER.get();
			case BASIC -> AMBlocks.BASIC_BUFFER.get();
			case ADVANCED -> AMBlocks.ADVANCED_BUFFER.get();
			case ELITE -> AMBlocks.ELITE_BUFFER.get();
			case CREATIVE -> AMBlocks.CREATIVE_BUFFER.get();
		};
	}
	
	@Override
	public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new BufferScreenHandler(syncId, playerInventory.player, pos);
	}
	
	@Override
	public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {
		buffer.writeBlockPos(pos);
	}
	
	public static class Primitive extends BufferBlock {
		public Primitive(Settings settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new BufferBlockEntity.Primitive(pos, state);
		}
		
		@Override
		public MachineTier getTier() {
			return MachineTier.PRIMITIVE;
		}
	}
	
	public static class Basic extends BufferBlock {
		public Basic(Settings settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new BufferBlockEntity.Basic(pos, state);
		}
		
		@Override
		public MachineTier getTier() {
			return MachineTier.BASIC;
		}
	}
	
	public static class Advanced extends BufferBlock {
		public Advanced(Settings settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new BufferBlockEntity.Advanced(pos, state);
		}
		
		@Override
		public MachineTier getTier() {
			return MachineTier.ADVANCED;
		}
	}
	
	public static class Elite extends BufferBlock {
		public Elite(Settings settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new BufferBlockEntity.Elite(pos, state);
		}
		
		@Override
		public MachineTier getTier() {
			return MachineTier.ELITE;
		}
	}
	
	public static class Creative extends BufferBlock {
		public Creative(Settings settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new BufferBlockEntity.Creative(pos, state);
		}
		
		@Override
		public MachineTier getTier() {
			return MachineTier.CREATIVE;
		}
	}
}
