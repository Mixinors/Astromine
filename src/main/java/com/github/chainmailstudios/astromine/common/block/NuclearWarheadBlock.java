package com.github.chainmailstudios.astromine.common.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.container.VentContainer;
import com.github.chainmailstudios.astromine.common.miscellaneous.ExplosionAlgorithm;

public class NuclearWarheadBlock extends Block {
	public NuclearWarheadBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		this.tryDetonate(world, pos);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!(!world.isClient && FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)) {
			this.tryDetonate(world, pos);
		}
	}

	private void tryDetonate(World world, BlockPos pos) {
		if (world.isReceivingRedstonePower(pos)) {
			ExplosionAlgorithm.tryExploding(world, pos.getX(), pos.getY(), pos.getZ(), 128);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient && !(player.getStackInHand(hand).getItem() instanceof BucketItem)) {
			player.openHandledScreen(new ExtendedScreenHandlerFactory() {
				@Override
				public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buffer) {
					buffer.writeBlockPos(blockPos);
				}

				@Override
				public Text getDisplayName() {
					return new TranslatableText("block.astromine.nuclear_warhead");
				}

				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
					return new VentContainer(syncId, playerInventory, blockPos);
				}
			});

			return ActionResult.CONSUME;
		} else if (player.getStackInHand(hand).getItem() instanceof BucketItem) {
			return super.onUse(state, world, blockPos, player, hand, hit);
		} else {
			return ActionResult.SUCCESS;
		}
	}
}
