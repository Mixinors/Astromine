package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.TrituratorBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.TrituratorScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public abstract class TrituratorBlock extends TieredHorizontalFacingMachineBlock {
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
			return new TrituratorScreenHandler(syncId, playerInventory, pos);
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
			return 0.5;
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
			return 1;
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
			return 2;
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
			return 4;
		}
	}
}
