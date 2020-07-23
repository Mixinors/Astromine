/*
 * MIT License
 * 
 * Copyright (c) 2020 Chainmail Studios
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.chainmailstudios.astromine.common.block.base;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;


public abstract class DefaultedBlockWithEntity extends Block implements BlockEntityProvider {
	protected DefaultedBlockWithEntity(AbstractBlock.Settings settings) {
		super(settings);
	}

	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient && !(player.getStackInHand(hand).getItem() instanceof BucketItem) && hasScreenHandler()) {
			player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
			return ActionResult.CONSUME;
		} else if (player.getStackInHand(hand).getItem() instanceof BucketItem) {
			return super.onUse(state, world, pos, player, hand, hit);
		} else {
			return ActionResult.SUCCESS;
		}
	}

	public abstract boolean hasScreenHandler();

	public abstract BlockEntity createBlockEntity();

	public abstract ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player);

	public abstract void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer);

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return createBlockEntity();
	}

	@Override
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return new ExtendedScreenHandlerFactory() {
			@Override
			public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buffer) {
				populateScreenHandlerBuffer(state, world, pos, player, buffer);
			}

			@Override
			public Text getDisplayName() {
				return new TranslatableText(getTranslationKey());
			}

			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
				return createScreenHandler(state, world, pos, syncId, playerInventory, player);
			}
		};
	}

	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		super.onSyncedBlockEvent(state, world, pos, type, data);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
		super.appendProperties(builder);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(ACTIVE, false);
	}

	public static void markActive(World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(ACTIVE, true));
	}

	public static void markInactive(World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(ACTIVE, false));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);

		CompoundTag tag = itemStack.getOrCreateTag();

		if (tag.contains("tracker")) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			BlockPos oldPos = blockEntity.getPos();
			blockEntity.fromTag(state, itemStack.getOrCreateTag());
			blockEntity.setPos(oldPos);
		}
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
		player.incrementStat(Stats.MINED.getOrCreateStat(this));
		player.addExhaustion(0.005F);

		boolean isTagged = false;

		if (world instanceof ServerWorld) {
			for (ItemStack drop : getDroppedStacks(state, (ServerWorld) world, pos, blockEntity, player, stack)) {
				if (!isTagged && drop.getItem() == asItem()) {
					drop.setTag(blockEntity.toTag(drop.getOrCreateTag()));
					drop.getTag().putByte("tracker", (byte) 0);
					isTagged = true;
				}

				dropStack(world, pos, drop);
			}
		}

		state.onStacksDropped(world, pos, stack);
	}
}
