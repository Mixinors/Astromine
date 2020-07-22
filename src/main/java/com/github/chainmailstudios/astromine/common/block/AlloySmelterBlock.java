package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.AlloySmelterBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.AlloySmelterScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AlloySmelterBlock extends TieredHorizontalFacingMachineBlock {
	public AlloySmelterBlock(Settings settings) {
		super(settings);
	}

	public abstract static class Base extends AlloySmelterBlock {
		public Base(Settings settings) {
			super(settings);
		}

		@Override
		public boolean hasScreenHandler() {
			return true;
		}

		@Override
		public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
			return new AlloySmelterScreenHandler(syncId, playerInventory, pos);
		}

		@Override
		public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {
			buffer.writeBlockPos(pos);
		}
	}

	public static class Primitive extends AlloySmelterBlock.Base {
		public Primitive(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new AlloySmelterBlockEntity.Primitive();
		}

		@Override
		public double getMachineSpeed() {
			return 0.5;
		}
	}

	public static class Basic extends AlloySmelterBlock.Base {
		public Basic(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new AlloySmelterBlockEntity.Basic();
		}

		@Override
		public double getMachineSpeed() {
			return 1;
		}
	}

	public static class Advanced extends AlloySmelterBlock.Base {
		public Advanced(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new AlloySmelterBlockEntity.Advanced();
		}

		@Override
		public double getMachineSpeed() {
			return 2;
		}
	}

	public static class Elite extends AlloySmelterBlock.Base {
		public Elite(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new AlloySmelterBlockEntity.Elite();
		}

		@Override
		public double getMachineSpeed() {
			return 4;
		}
	}
}
