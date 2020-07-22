package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.PresserBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.PresserScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class PresserBlock extends TieredHorizontalFacingMachineBlock {
	public PresserBlock(Settings settings) {
		super(settings);
	}

	public abstract static class Base extends PresserBlock {
		public Base(Settings settings) {
			super(settings);
		}

		@Override
		public boolean hasScreenHandler() {
			return true;
		}

		@Override
		public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
			return new PresserScreenHandler(syncId, playerInventory, pos);
		}

		@Override
		public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {
			buffer.writeBlockPos(pos);
		}
	}

	public static class Primitive extends PresserBlock.Base {
		public Primitive(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new PresserBlockEntity.Primitive();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitivePresserSpeed;
		}
	}

	public static class Basic extends PresserBlock.Base {
		public Basic(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new PresserBlockEntity.Basic();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicPresserSpeed;
		}
	}

	public static class Advanced extends PresserBlock.Base {
		public Advanced(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new PresserBlockEntity.Advanced();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedPresserSpeed;
		}
	}

	public static class Elite extends PresserBlock.Base {
		public Elite(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new PresserBlockEntity.Elite();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().elitePresserSpeed;
		}
	}
}
