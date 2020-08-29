/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.technologies.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.block.base.WrenchableHorizontalFacingEnergyBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.base.WrenchableHorizontalFacingEnergyTieredBlockWithEntity;
import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.CapacitorBlockEntity;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.CreativeCapacitorBlockEntity;
import com.github.chainmailstudios.astromine.technologies.common.screenhandler.CapacitorScreenHandler;
import com.github.chainmailstudios.astromine.technologies.common.screenhandler.CreativeCapacitorScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;

public abstract class CapacitorBlock extends WrenchableHorizontalFacingEnergyTieredBlockWithEntity {
	public CapacitorBlock(Settings settings) {
		super(settings);
	}

	@Override
	public double getMachineSpeed() {
		return 0;
	}

	public abstract static class Base extends CapacitorBlock {
		public Base(Settings settings) {
			super(settings);
		}

		@Override
		public boolean hasScreenHandler() {
			return true;
		}

		@Override
		public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
			return new CapacitorScreenHandler(syncId, playerInventory.player, pos);
		}

		@Override
		public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {
			buffer.writeBlockPos(pos);
		}

		@Override
		public boolean showSpeedInTooltip() {
			return false;
		}
	}

	public static class Primitive extends CapacitorBlock.Base {
		public Primitive(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new CapacitorBlockEntity.Primitive();
		}

		@Override
		public double getEnergyCapacity() {
			return AstromineConfig.get().primitiveCapacitorEnergy;
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends CapacitorBlock.Base {
		public Basic(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new CapacitorBlockEntity.Basic();
		}

		@Override
		public double getEnergyCapacity() {
			return AstromineConfig.get().basicCapacitorEnergy;
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends CapacitorBlock.Base {
		public Advanced(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new CapacitorBlockEntity.Advanced();
		}

		@Override
		public double getEnergyCapacity() {
			return AstromineConfig.get().advancedCapacitorEnergy;
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends CapacitorBlock.Base {
		public Elite(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new CapacitorBlockEntity.Elite();
		}

		@Override
		public double getEnergyCapacity() {
			return AstromineConfig.get().eliteCapacitorEnergy;
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.ELITE;
		}
	}

	public static class Creative extends CapacitorBlock.Base {
		public Creative(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new CreativeCapacitorBlockEntity();
		}

		@Override
		public double getEnergyCapacity() {
			return Integer.MAX_VALUE;
		}

		@Override
		public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
			return new CreativeCapacitorScreenHandler(syncId, playerInventory.player, pos);
		}

		@Override
		public boolean isCreative() {
			return true;
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.CREATIVE;
		}
	}
}
