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

import com.github.mixinors.astromine.common.block.entity.base.Tickable;
import com.github.mixinors.astromine.common.item.storage.SimpleEnergyStorageItem;
import com.github.mixinors.astromine.common.item.storage.SimpleFluidStorageItem;
import com.github.mixinors.astromine.common.util.data.redstone.ComparatorMode;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Block} with an attached {@link BlockEntity} provided through {@link BlockEntityProvider}, providing {@link #ACTIVE} {@link BlockState} property by default.
 */
public abstract class BlockWithEntity extends Block implements BlockEntityProvider {
	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
	
	public static final SavedData ITEM_MACHINE = new SavedData(true, true, true, false);
	public static final SavedData FLUID_MACHINE = new SavedData(true, true, false, true);
	public static final SavedData ITEM_AND_FLUID_MACHINE = new SavedData(true, true, true, true);
	
	/** Instantiates a {@link BlockWithEntity}. */
	protected BlockWithEntity(AbstractBlock.Settings settings) {
		super(settings);
	}
	
	/**
	 * Sets the {@link BlockState} at the {@link BlockPos} in the given {@link World} to have {@link #ACTIVE} as true.
	 */
	public static void markActive(World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(ACTIVE, true));
	}
	
	/**
	 * Sets the {@link BlockState} at the {@link BlockPos} in the given {@link World} to have {@link #ACTIVE} as false.
	 */
	public static void markInactive(World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(ACTIVE, false));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient && (!(player.getStackInHand(hand).getItem() instanceof BucketItem) && !(player.getStackInHand(hand).getItem() instanceof SimpleEnergyStorageItem) && !(player.getStackInHand(hand).getItem() instanceof SimpleFluidStorageItem)) && hasScreenHandler()) {
			MenuRegistry.openExtendedMenu((ServerPlayerEntity) player, createScreenHandlerFactory((ServerPlayerEntity) player, state, world, pos));
			
			return ActionResult.CONSUME;
		} else if (player.getStackInHand(hand).getItem() instanceof BucketItem) {
			return super.onUse(state, world, pos, player, hand, hit);
		} else {
			return ActionResult.SUCCESS;
		}
	}
	
	/**
	 * Asserts whether this {@link BlockWithEntity} has a {@link ScreenHandler} or not.
	 */
	public abstract boolean hasScreenHandler();
	
	/**
	 * Returns the {@link ScreenHandler} this {@link Block} will open.
	 */
	public abstract ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player);
	
	/**
	 * Populates the {@link PacketByteBuf} which will be passed onto {@link ExtendedMenuProvider#saveExtraData(PacketByteBuf)}.
	 */
	public abstract void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer);
	
	/**
	 * Returns the {@link ScreenHandlerFactory} this {@link Block} will use.
	 */
	public ExtendedMenuProvider createScreenHandlerFactory(ServerPlayerEntity player, BlockState state, World world, BlockPos pos) {
		return new ExtendedMenuProvider() {
			/** Writes data from {@link BlockWithEntity#populateScreenHandlerBuffer(BlockState, World, BlockPos, ServerPlayerEntity, PacketByteBuf)}
			 * to the given {@link PacketByteBuf}. */
			@Override
			public void saveExtraData(PacketByteBuf buffer) {
				populateScreenHandlerBuffer(state, world, pos, player, buffer);
			}
			
			/** Returns the name of the created {@link ScreenHandler}. */
			@Override
			public Text getDisplayName() {
				return new TranslatableText(getTranslationKey());
			}
			
			/** Returns the created {@link ScreenHandler}. */
			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
				return createScreenHandler(state, world, pos, syncId, playerInventory, player);
			}
		};
	}
	
	/**
	 * Repasses the synced block event to the {@link BlockEntity} in the given {@link World} at the given {@link BlockPos}.
	 */
	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		super.onSyncedBlockEvent(state, world, pos, type, data);
		
		var blockEntity = world.getBlockEntity(pos);
		
		return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
	}
	
	/** Override behavior to add the {@link #ACTIVE} property. */
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
		super.appendProperties(builder);
	}
	
	/** Override behavior to set {@link #ACTIVE} to false by default. */
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(ACTIVE, false);
	}
	
	/** Return this {@link BlockWithEntity}'s {@link ComparatorMode}. */
	protected ComparatorMode getComparatorMode() {
		return ComparatorMode.ITEMS;
	}
	
	/** Override behavior to use {@link ComparatorMode}. */
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return getComparatorMode().hasOutput();
	}
	
	/** Override behavior to use {@link ComparatorMode}. */
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return getComparatorMode().getOutput(world.getBlockEntity(pos));
	}
	
	/** Override behavior to read {@link BlockEntity} contents from {@link ItemStack} {@link NbtCompound}. */
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onPlaced(world, pos, state, placer, stack);
		
		var blockEntity = world.getBlockEntity(pos);
		
		if (blockEntity != null) {
			blockEntity.readNbt(stack.getOrCreateNbt());
		}
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		// We might not need this, but whatever, we are astromine
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
	
	public record SavedData(boolean redstoneControl, boolean energyStorage, boolean itemStorage, boolean fluidStorage) {}
}
