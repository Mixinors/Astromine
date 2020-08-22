package com.github.chainmailstudios.astromine.foundations.common.block;

import com.github.chainmailstudios.astromine.common.block.base.WrenchableBlockWithEntity;
import com.github.chainmailstudios.astromine.foundations.common.block.altar.entity.ItemDisplayerBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ItemDisplayerBlock extends WrenchableBlockWithEntity {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

	public ItemDisplayerBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean hasScreenHandler() {
		return false;
	}

	@Override
	public BlockEntity createBlockEntity() {
		return new ItemDisplayerBlockEntity();
	}

	@Override
	public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return null;
	}

	@Override
	public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {

	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			ItemStack stackInHand = player.getStackInHand(hand);

			if (blockEntity instanceof ItemDisplayerBlockEntity) {
				ItemDisplayerBlockEntity displayerBlockEntity = (ItemDisplayerBlockEntity) blockEntity;
				if (displayerBlockEntity.getStack(0).isEmpty()) {
					if (!stackInHand.isEmpty()) {
						displayerBlockEntity.setStack(0, stackInHand.split(1));
						displayerBlockEntity.sync();
						return ActionResult.SUCCESS;
					}
					return ActionResult.CONSUME;
				} else if (canMergeItems(stackInHand, displayerBlockEntity.getStack(0))) {
					ItemStack copy = stackInHand.copy();
					copy.increment(1);
					player.setStackInHand(hand, copy);
					displayerBlockEntity.setStack(0, ItemStack.EMPTY);
					player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, .6F, 1);
					displayerBlockEntity.sync();
				} else if (stackInHand.isEmpty()) {
					player.setStackInHand(hand, displayerBlockEntity.getStack(0).copy());
					displayerBlockEntity.setStack(0, ItemStack.EMPTY);
					player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, .6F, 1);
					displayerBlockEntity.sync();
				} else {
					return ActionResult.CONSUME;
				}
			}
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	public static boolean canMergeItems(ItemStack first, ItemStack second) {
		if (first.getItem() != second.getItem()) {
			return false;
		} else if (first.getDamage() != second.getDamage()) {
			return false;
		} else if (first.getCount() >= first.getMaxCount()) {
			return false;
		} else {
			return ItemStack.areTagsEqual(first, second);
		}
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
		if (blockEntity instanceof ItemDisplayerBlockEntity) {
			((ItemDisplayerBlockEntity) blockEntity).onRemove();
		}
		super.onBreak(world, pos, state, player);
	}
}
