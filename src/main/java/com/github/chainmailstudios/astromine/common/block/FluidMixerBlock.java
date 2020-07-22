package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.FluidMixerBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.FluidMixerScreenHandler;
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

public abstract class FluidMixerBlock extends TieredHorizontalFacingMachineBlock {
	public FluidMixerBlock(Settings settings) {
		super(settings);
	}

	public abstract static class Base extends FluidMixerBlock {
		public Base(Settings settings) {
			super(settings);
		}

		@Override
		public boolean hasScreenHandler() {
			return true;
		}

		@Override
		public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
			return new FluidMixerScreenHandler(syncId, playerInventory, pos);
		}

		@Override
		public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {
			buffer.writeBlockPos(pos);
		}
	}

	public static class Primitive extends FluidMixerBlock.Base {
		public Primitive(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new FluidMixerBlockEntity.Primitive();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitiveFluidMixerSpeed;
		}
	}

	public static class Basic extends FluidMixerBlock.Base {
		public Basic(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new FluidMixerBlockEntity.Basic();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicFluidMixerSpeed;
		}
	}

	public static class Advanced extends FluidMixerBlock.Base {
		public Advanced(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new FluidMixerBlockEntity.Advanced();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedFluidMixerSpeed;
		}
	}

	public static class Elite extends FluidMixerBlock.Base {
		public Elite(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new FluidMixerBlockEntity.Elite();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().eliteFluidMixerSpeed;
		}
	}
}
