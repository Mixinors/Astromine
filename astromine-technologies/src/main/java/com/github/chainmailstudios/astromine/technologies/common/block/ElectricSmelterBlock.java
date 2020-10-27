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

import com.github.chainmailstudios.astromine.common.block.base.WrenchableHorizontalFacingTieredBlockWithEntity;
import com.github.chainmailstudios.astromine.common.network.NetworkBlock;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.ElectricSmelterBlockEntity;
import com.github.chainmailstudios.astromine.technologies.common.screenhandler.ElectricSmelterScreenHandler;

public abstract class ElectricSmelterBlock extends WrenchableHorizontalFacingTieredBlockWithEntity implements NetworkBlock.EnergyRequester {
	public ElectricSmelterBlock(Settings settings) {
		super(settings);
	}

	public abstract static class Base extends ElectricSmelterBlock {
		public Base(Settings settings) {
			super(settings);
		}

		@Override
		public boolean hasScreenHandler() {
			return true;
		}

		@Override
		public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
			return new ElectricSmelterScreenHandler(syncId, playerInventory.player, pos);
		}

		@Override
		public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {
			buffer.writeBlockPos(pos);
		}
	}

	public static class Primitive extends ElectricSmelterBlock.Base {
		public Primitive(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new ElectricSmelterBlockEntity.Primitive();
		}
	}

	public static class Basic extends ElectricSmelterBlock.Base {
		public Basic(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new ElectricSmelterBlockEntity.Basic();
		}
	}

	public static class Advanced extends ElectricSmelterBlock.Base {
		public Advanced(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new ElectricSmelterBlockEntity.Advanced();
		}
	}

	public static class Elite extends ElectricSmelterBlock.Base {
		public Elite(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new ElectricSmelterBlockEntity.Elite();
		}
	}
}
