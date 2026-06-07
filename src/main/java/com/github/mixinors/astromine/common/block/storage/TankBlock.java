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
import com.github.mixinors.astromine.common.block.entity.storage.TankBlockEntity;
import com.github.mixinors.astromine.common.comparator.ComparatorMode;
import com.github.mixinors.astromine.common.screen.handler.storage.TankScreenHandler;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TankBlock extends HorizontalFacingTieredBlockWithEntity {
	public TankBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public SavedData getSavedDataForDroppedItem() {
		return new SavedData(true, false, true, true);
	}
	
	@Override
	protected ComparatorMode getComparatorMode() {
		return ComparatorMode.FLUIDS;
	}
	
	@Override
	public Block getForTier(Tier tier) {
		return switch (tier) {
			case PRIMITIVE -> AMBlocks.PRIMITIVE_TANK.get();
			case BASIC -> AMBlocks.BASIC_TANK.get();
			case ADVANCED -> AMBlocks.ADVANCED_TANK.get();
			case ELITE -> AMBlocks.ELITE_TANK.get();
			case CREATIVE -> null;
		};
	}
	
	@Override
	public boolean hasScreenHandler() {
		return true;
	}
	
	@Override
	public AbstractContainerMenu createScreenHandler(BlockState state, Level world, BlockPos pos, int syncId, Inventory playerInventory, Player player) {
		return new TankScreenHandler(syncId, playerInventory.player, pos);
	}
	
	@Override
	public void populateScreenHandlerBuffer(BlockState state, Level world, BlockPos pos, ServerPlayer player, FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
	}
	
	public static class Primitive extends TankBlock {
		public Primitive(Properties settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
			return new TankBlockEntity.Primitive(pos, state);
		}
		
		@Override
		public Tier getTier() {
			return Tier.PRIMITIVE;
		}
	}
	
	public static class Basic extends TankBlock {
		public Basic(Properties settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
			return new TankBlockEntity.Basic(pos, state);
		}
		
		@Override
		public Tier getTier() {
			return Tier.BASIC;
		}
	}
	
	public static class Advanced extends TankBlock {
		public Advanced(Properties settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
			return new TankBlockEntity.Advanced(pos, state);
		}
		
		@Override
		public Tier getTier() {
			return Tier.ADVANCED;
		}
	}
	
	public static class Elite extends TankBlock {
		public Elite(Properties settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
			return new TankBlockEntity.Elite(pos, state);
		}
		
		@Override
		public Tier getTier() {
			return Tier.ELITE;
		}
	}
	
	public static class Creative extends TankBlock {
		public Creative(Properties settings) {
			super(settings);
		}
		
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
			return new TankBlockEntity.Creative(pos, state);
		}
		
		@Override
		public Tier getTier() {
			return Tier.CREATIVE;
		}
	}
}
