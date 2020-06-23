package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.LiquidGeneratorBlockEntity;
import com.github.chainmailstudios.astromine.common.container.LiquidGeneratorContainer;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LiquidGeneratorBlock extends HorizontalFacingBlock implements NetworkMember, BlockEntityProvider {
	public LiquidGeneratorBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		super.appendProperties(builder);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new LiquidGeneratorBlockEntity();
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.FLUID || type == AstromineNetworkTypes.ENERGY;
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
					return new TranslatableText("text.astromine.title.liquid_generator");
				}

				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
					return new LiquidGeneratorContainer(syncId, playerInventory, blockPos);
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
