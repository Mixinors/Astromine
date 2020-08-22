package com.github.chainmailstudios.astromine.foundations.common.block;

import com.github.chainmailstudios.astromine.common.block.base.WrenchableBlockWithEntity;
import com.github.chainmailstudios.astromine.foundations.common.block.altar.entity.AltarBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AltarBlock extends WrenchableBlockWithEntity {
	public AltarBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean hasScreenHandler() {
		return false;
	}

	@Override
	public BlockEntity createBlockEntity() {
		return new AltarBlockEntity();
	}

	@Override
	public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return null;
	}

	@Override
	public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {

	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			AltarBlockEntity blockEntity = (AltarBlockEntity) world.getBlockEntity(pos);
			ItemStack stackInHand = player.getStackInHand(hand);

			if (blockEntity.getStack(0).isEmpty()) {
				if (blockEntity.initializeCrafting()) {
					return ActionResult.SUCCESS;
				} else {
					return ActionResult.CONSUME;
				}
			} else if (ItemDisplayerBlock.canMergeItems(stackInHand, blockEntity.getStack(0))) {
				ItemStack copy = stackInHand.copy();
				copy.increment(1);
				player.setStackInHand(hand, copy);
				blockEntity.setStack(0, ItemStack.EMPTY);
				player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, .6F, 1);
				blockEntity.sync();
				return ActionResult.SUCCESS;
			} else if (stackInHand.isEmpty()) {
				player.setStackInHand(hand, blockEntity.getStack(0).copy());
				blockEntity.setStack(0, ItemStack.EMPTY);
				player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, .6F, 1);
				blockEntity.sync();
				return ActionResult.SUCCESS;
			} else {
				return ActionResult.CONSUME;
			}
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		boolean power = world.isReceivingRedstonePower(pos);
		if (!world.isClient) {
			AltarBlockEntity blockEntity = (AltarBlockEntity) world.getBlockEntity(pos);

			if (blockEntity.getStack(0).isEmpty()) {
				blockEntity.initializeCrafting();
			}
		}
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
	}

	@Override
	protected boolean saveTagToDroppedItem() {
		return false;
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Inventory) {
				ItemScatterer.spawn(world, pos.add(0, 1, 0), (Inventory) blockEntity);
				world.updateComparators(pos, this);
			}

			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof AltarBlockEntity) {
			((AltarBlockEntity) blockEntity).onRemove();
		}
		super.onBreak(world, pos, state, player);
	}
}
