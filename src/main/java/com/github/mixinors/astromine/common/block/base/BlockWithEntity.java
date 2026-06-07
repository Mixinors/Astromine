/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.block.base;

import com.github.mixinors.astromine.common.comparator.ComparatorMode;
import com.github.mixinors.astromine.common.item.storage.SimpleEnergyStorageItem;
import com.github.mixinors.astromine.common.item.storage.SimpleFluidStorageItem;
import com.github.mixinors.astromine.common.tick.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class BlockWithEntity extends Block implements EntityBlock {
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	
	public static final SavedData MACHINE = new SavedData(false, false, false, false);
	public static final SavedData ITEM_MACHINE = new SavedData(true, true, true, false);
	public static final SavedData FLUID_MACHINE = new SavedData(true, true, false, true);
	public static final SavedData ITEM_AND_FLUID_MACHINE = new SavedData(true, true, true, true);
	
	protected BlockWithEntity(BlockBehaviour.Properties settings) {
		super(settings);
	}
	
	public static void markActive(Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(ACTIVE, true));
	}
	
	public static void markInactive(Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(ACTIVE, false));
	}
	
	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!world.isClientSide && (!(stack.getItem() instanceof BucketItem) && !(stack.getItem() instanceof SimpleEnergyStorageItem) && !(stack.getItem() instanceof SimpleFluidStorageItem)) && hasScreenHandler()) {
			var serverPlayer = (ServerPlayer) player;
			
			serverPlayer.openMenu(createScreenHandlerFactory(serverPlayer, state, world, pos), buffer -> populateScreenHandlerBuffer(state, world, pos, serverPlayer, buffer));
			
			return ItemInteractionResult.CONSUME;
		} else if (stack.getItem() instanceof BucketItem) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		} else {
			return ItemInteractionResult.SUCCESS;
		}
	}
	
	public abstract boolean hasScreenHandler();
	
	public abstract AbstractContainerMenu createScreenHandler(BlockState state, Level world, BlockPos pos, int syncId, Inventory playerInventory, Player player);
	
	public abstract void populateScreenHandlerBuffer(BlockState state, Level world, BlockPos pos, ServerPlayer player, FriendlyByteBuf buffer);
	
	public MenuProvider createScreenHandlerFactory(ServerPlayer player, BlockState state, Level world, BlockPos pos) {
		return new MenuProvider() {
			@Override
			public Component getDisplayName() {
				return Component.translatable(getDescriptionId());
			}
			
			@Override
			public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
				return createScreenHandler(state, world, pos, syncId, playerInventory, player);
			}
		};
	}
	
	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int type, int data) {
		super.triggerEvent(state, world, pos, type, data);
		
		var blockEntity = world.getBlockEntity(pos);
		
		return blockEntity != null && blockEntity.triggerEvent(type, data);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
		
		super.createBlockStateDefinition(builder);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(ACTIVE, false);
	}
	
	protected ComparatorMode getComparatorMode() {
		return ComparatorMode.ITEMS;
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return getComparatorMode().hasOutput();
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return getComparatorMode().getOutput(world.getBlockEntity(pos));
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(world, pos, state, placer, stack);
		
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return (world1, blockPos, blockState, blockEntity) -> {
			if (blockEntity instanceof Tickable tickableBlockEntity) {
				tickableBlockEntity.tick();
			}
		};
	}
	
	public boolean saveTagToDroppedItem() {
		return true;
	}
	
	public abstract SavedData getSavedDataForDroppedItem();
	
	public record SavedData(
			boolean redstoneControl,
			boolean energyStorage,
			boolean itemStorage,
			boolean fluidStorage
	) {}
}
