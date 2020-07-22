package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.LiquidGeneratorBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.LiquidGeneratorScreenHandler;
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

public abstract class LiquidGeneratorBlock extends TieredHorizontalFacingMachineBlock {
	public LiquidGeneratorBlock(Settings settings) {
		super(settings);
	}

	public abstract static class Base extends LiquidGeneratorBlock {
		public Base(Settings settings) {
			super(settings);
		}

		@Override
		public boolean hasScreenHandler() {
			return true;
		}

		@Override
		public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
			return new LiquidGeneratorScreenHandler(syncId, playerInventory, pos);
		}

		@Override
		public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {
			buffer.writeBlockPos(pos);
		}
	}

	public static class Primitive extends LiquidGeneratorBlock.Base {
		public Primitive(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new LiquidGeneratorBlockEntity.Primitive();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitiveLiquidGeneratorSpeed;
		}
	}

	public static class Basic extends LiquidGeneratorBlock.Base {
		public Basic(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new LiquidGeneratorBlockEntity.Basic();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicLiquidGeneratorSpeed;
		}
	}

	public static class Advanced extends LiquidGeneratorBlock.Base {
		public Advanced(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new LiquidGeneratorBlockEntity.Advanced();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedLiquidGeneratorSpeed;
		}
	}

	public static class Elite extends LiquidGeneratorBlock.Base {
		public Elite(Settings settings) {
			super(settings);
		}

		@Override
		public BlockEntity createBlockEntity() {
			return new LiquidGeneratorBlockEntity.Elite();
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().eliteLiquidGeneratorSpeed;
		}
	}
}
