package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedHorizontalFacingBlock;
import com.github.chainmailstudios.astromine.common.block.entity.SmelterBlockEntity;
import com.github.chainmailstudios.astromine.common.block.entity.SorterBlockEntity;
import com.github.chainmailstudios.astromine.common.container.SmelterContainer;
import com.github.chainmailstudios.astromine.common.container.SorterContainer;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SmelterBlock extends DefaultedHorizontalFacingBlock implements NetworkMember, BlockEntityProvider {
	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

	public SmelterBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isRequester() {
		return true;
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.ENERGY;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new SmelterBlockEntity();
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
					return new TranslatableText("text.astromine.title.smelter");
				}

				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
					return new SmelterContainer(syncId, playerInventory, blockPos);
				}
			});

			return ActionResult.CONSUME;
		} else if (player.getStackInHand(hand).getItem() instanceof BucketItem) {
			return super.onUse(state, world, blockPos, player, hand, hit);
		} else {
			return ActionResult.SUCCESS;
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
		super.appendProperties(builder);
	}
}
