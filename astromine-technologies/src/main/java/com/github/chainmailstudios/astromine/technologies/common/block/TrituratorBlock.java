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

import com.github.chainmailstudios.astromine.common.block.base.WrenchableHorizontalFacingEnergyTieredBlockWithEntity;
import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.TrituratorBlockEntity;
import com.github.chainmailstudios.astromine.technologies.common.screenhandler.TrituratorScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;

public abstract class TrituratorBlock extends WrenchableHorizontalFacingEnergyTieredBlockWithEntity {
	public TrituratorBlock(Settings settings) {
		super(settings);
	}

	public abstract static class Base extends TrituratorBlock {
		public Base(Settings settings) {
			super(settings);
		}

		@Override
		public boolean hasScreenHandler() {
			return true;
		}

		@Override
		public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
			return new TrituratorScreenHandler(syncId, playerInventory.player, pos);
		}

		@Override
		public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {
			buffer.writeBlockPos(pos);
		}
	}

	public static class Primitive extends TrituratorBlock.Base {
		public Primitive(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new TrituratorBlockEntity.Primitive();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitiveTrituratorSpeed;
		}

		@Override
		public double getEnergyCapacity() {
			return AstromineConfig.get().primitiveTrituratorEnergy;
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends TrituratorBlock.Base {
		public Basic(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new TrituratorBlockEntity.Basic();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicTrituratorSpeed;
		}

		@Override
		public double getEnergyCapacity() {
			return AstromineConfig.get().basicTrituratorEnergy;
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends TrituratorBlock.Base {
		public Advanced(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new TrituratorBlockEntity.Advanced();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedTrituratorSpeed;
		}

		@Override
		public double getEnergyCapacity() {
			return AstromineConfig.get().advancedTrituratorEnergy;
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends TrituratorBlock.Base {
		public Elite(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new TrituratorBlockEntity.Elite();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().eliteTrituratorSpeed;
		}

		@Override
		public double getEnergyCapacity() {
			return AstromineConfig.get().eliteTrituratorEnergy;
		}

		@Override
		public MachineTier getTier() {
			return MachineTier.ELITE;
		}
	}
}
