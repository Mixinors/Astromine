package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.miscellaneous.ExplosionAlgorithm;
import com.github.chainmailstudios.astromine.common.screenhandler.VentScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
		if (world.isClient()) return;
		if (!AstromineConfig.get().nuclearWarheadEnabled) return;
		if (world.isReceivingRedstonePower(pos)) {
			ExplosionAlgorithm.tryExploding(world, pos.getX(), pos.getY(), pos.getZ(), 128);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient && !(player.getStackInHand(hand).getItem() instanceof BucketItem)) {
			player.openHandledScreen(state.createScreenHandlerFactory(world, blockPos));
			return ActionResult.CONSUME;
		} else if (player.getStackInHand(hand).getItem() instanceof BucketItem) {
			return super.onUse(state, world, blockPos, player, hand, hit);
		} else {
			return ActionResult.SUCCESS;
		}
	}

	@Override
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return new ExtendedScreenHandlerFactory() {
			@Override
			public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buffer) {
				buffer.writeBlockPos(pos);
			}

			@Override
			public Text getDisplayName() {
				return new TranslatableText(getTranslationKey());
			}

			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
				return new VentScreenHandler(syncId, playerInventory, pos);
			}
		};
	}
}
