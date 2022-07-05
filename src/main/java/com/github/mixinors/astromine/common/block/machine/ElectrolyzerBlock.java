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
import com.github.mixinors.astromine.common.block.entity.machine.ElectrolyzerBlockEntity;
import com.github.mixinors.astromine.common.comparator.ComparatorMode;
import com.github.mixinors.astromine.common.screen.handler.machine.ElectrolyzerScreenHandler;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
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

public abstract class ElectrolyzerBlock extends HorizontalFacingTieredBlockWithEntity {
	public ElectrolyzerBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public SavedData getSavedDataForDroppedItem() {
		return FLUID_MACHINE;
	}
	
	@Override
	protected ComparatorMode getComparatorMode() {
		return ComparatorMode.FLUIDS;
	}
	
	@Override
	public Block getForTier(Tier tier) {
		return switch (tier) {
			case PRIMITIVE -> AMBlocks.PRIMITIVE_ELECTROLYZER.get();
			case BASIC -> AMBlocks.BASIC_ELECTROLYZER.get();
			case ADVANCED -> AMBlocks.ADVANCED_ELECTROLYZER.get();
			case ELITE -> AMBlocks.ELITE_ELECTROLYZER.get();
			case CREATIVE -> null;
		};
	}
	
	@Override
	public boolean hasScreenHandler() {
		return true;
	}
	
	@Override
	public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new ElectrolyzerScreenHandler(syncId, playerInventory.player, pos);
	}
	
	@Override
	public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {
		buffer.writeBlockPos(pos);
	}
	
	public static class Primitive extends ElectrolyzerBlock {
		public Primitive(Settings settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new ElectrolyzerBlockEntity.Primitive(pos, state);
		}
		
		@Override
		public Tier getTier() {
			return Tier.PRIMITIVE;
		}
	}
	
	public static class Basic extends ElectrolyzerBlock {
		public Basic(Settings settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new ElectrolyzerBlockEntity.Basic(pos, state);
		}
		
		@Override
		public Tier getTier() {
			return Tier.BASIC;
		}
	}
	
	public static class Advanced extends ElectrolyzerBlock {
		public Advanced(Settings settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new ElectrolyzerBlockEntity.Advanced(pos, state);
		}
		
		@Override
		public Tier getTier() {
			return Tier.ADVANCED;
		}
	}
	
	public static class Elite extends ElectrolyzerBlock {
		public Elite(Settings settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new ElectrolyzerBlockEntity.Elite(pos, state);
		}
		
		@Override
		public Tier getTier() {
			return Tier.ELITE;
		}
	}
}
