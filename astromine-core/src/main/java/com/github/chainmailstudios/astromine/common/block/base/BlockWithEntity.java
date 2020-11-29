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
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import com.github.chainmailstudios.astromine.common.block.redstone.ComparatorMode;
import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;

import java.util.List;

/**
 * A {@link Block} with an attached {@link BlockEntity} provided
 * through {@link EntityBlock}, providing {@link #ACTIVE}
 * {@link BlockState} property by default.
 */
public abstract class BlockWithEntity extends Block implements EntityBlock {
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	/** Instantiates a {@link BlockWithEntity}. */
	protected BlockWithEntity(BlockBehaviour.Properties settings) {
		super(settings);
	}

	/** Sets the {@link BlockState} at the {@link BlockPos} in
	 * in the given {@link Level} to have {@link #ACTIVE} as true. */
	public static void markActive(Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(ACTIVE, true));
	}

	/** Sets the {@link BlockState} at the {@link BlockPos} in
	 * in the given {@link Level} to have {@link #ACTIVE} as false. */
	public static void markInactive(Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(ACTIVE, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!world.isClientSide && (!(player.getItemInHand(hand).getItem() instanceof BucketItem) && !(player.getItemInHand(hand).getItem() instanceof EnergyVolumeItem) && !(player.getItemInHand(hand).getItem() instanceof FluidVolumeItem)) && hasScreenHandler()) {
			player.openMenu(state.getMenuProvider(world, pos));

			return InteractionResult.CONSUME;
		} else if (player.getItemInHand(hand).getItem() instanceof BucketItem) {
			return super.use(state, world, pos, player, hand, hit);
		} else {
			return InteractionResult.SUCCESS;
		}
	}

	/** Asserts whether this {@link BlockWithEntity} has
	 * a {@link AbstractContainerMenu} or not. */
	public abstract boolean hasScreenHandler();

	/** Returns the {@link BlockEntity} this {@link Block}
	 * will create. */
	public abstract BlockEntity createBlockEntity();

	/** Returns the {@link AbstractContainerMenu} this {@link Block}
	 * will open. */
	public abstract AbstractContainerMenu createScreenHandler(BlockState state, Level world, BlockPos pos, int syncId, Inventory playerInventory, Player player);

	/** Populates the {@link FriendlyByteBuf} which will be
	 * passed onto {@link ExtendedScreenHandlerFactory#writeScreenOpeningData(ServerPlayer, FriendlyByteBuf)}. */
	public abstract void populateScreenHandlerBuffer(BlockState state, Level world, BlockPos pos, ServerPlayer player, FriendlyByteBuf buffer);

	/** Returns the {@link BlockEntity} this {@link Block}
	 * will create. */
	@Override
	public BlockEntity newBlockEntity(BlockGetter world) {
		return createBlockEntity();
	}

	/** Returns the {@link MenuConstructor} this {@link Block}
	 * will use. */
	@Override
	public MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
		return new ExtendedScreenHandlerFactory() {
			/** Writes data from {@link BlockWithEntity#populateScreenHandlerBuffer(BlockState, Level, BlockPos, ServerPlayer, FriendlyByteBuf)}
			 * to the given {@link FriendlyByteBuf}. */
			@Override
			public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buffer) {
				populateScreenHandlerBuffer(state, world, pos, player, buffer);
			}

			/** Returns the name of the created {@link AbstractContainerMenu}. */
			@Override
			public Component getDisplayName() {
				return new TranslatableComponent(getDescriptionId());
			}

			/** Returns the created {@link AbstractContainerMenu}. */
			@Override
			public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
				return createScreenHandler(state, world, pos, syncId, playerInventory, player);
			}
		};
	}

	/** Repasses the synced block event to the {@link BlockEntity} in the
	 * given {@link Level} at the given {@link BlockPos}. */
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int type, int data) {
		super.triggerEvent(state, world, pos, type, data);

		BlockEntity blockEntity = world.getBlockEntity(pos);

		return blockEntity != null && blockEntity.triggerEvent(type, data);
	}

	/** Override behavior to add the {@link #ACTIVE} property. */
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
		super.createBlockStateDefinition(builder);
	}

	/** Override behavior to set {@link #ACTIVE} to false by default. */
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(ACTIVE, false);
	}

	/** Return this {@link BlockWithEntity}'s {@link ComparatorMode}. */
	protected ComparatorMode getComparatorMode() {
		return ComparatorMode.ITEMS;
	}

	/** Override behavior to use {@link ComparatorMode}. */
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return getComparatorMode().hasOutput();
	}

	/** Override behavior to use {@link ComparatorMode}. */
	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return getComparatorMode().getOutput(world.getBlockEntity(pos));
	}

	/** Override behavior to read {@link BlockEntity} contents from {@link ItemStack} {@link CompoundTag}. */
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(world, pos, state, placer, stack);

		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (blockEntity != null) {
			blockEntity.load(state, stack.getOrCreateTag());
			blockEntity.setPosition(pos);
		}
	}

	/** Override behavior to write {@link BlockEntity} contents to {@link ItemStack} {@link CompoundTag}. */
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> stacks = super.getDrops(state, builder);
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity != null && saveTagToDroppedItem()) {
			for (ItemStack drop : stacks) {
				if (drop.getItem() == asItem()) {
					CompoundTag tag = blockEntity.save(drop.getOrCreateTag());
					tag.remove("x");
					tag.remove("y");
					tag.remove("z");
					drop.setTag(tag);
					break;
				}
			}
		}
		return stacks;
	}

	protected boolean saveTagToDroppedItem() {
		return true;
	}
}
