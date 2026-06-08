/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.common.screen.handler.base.block.entity;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.MenuQuickMove;
import com.github.mixinors.astromine.common.screen.handler.base.block.BlockStateScreenHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public abstract class ExtendedBlockEntityScreenHandler extends BlockStateScreenHandler {
	private static final int PROGRESS_DATA_SCALE = 10000;

	protected final ExtendedBlockEntity blockEntity;
	private final ProgressContainerData progressData;
	protected final List<GuiArrow> arrows = new ArrayList<>();
	protected final List<GuiFluidBar> fluidBars = new ArrayList<>();
	protected final List<GuiFluidFilter> fluidFilters = new ArrayList<>();
	protected final List<GuiItemHint> itemHints = new ArrayList<>();
	protected int energyBarX = ExtendedBlockEntityMenuLayout.ENERGY_BAR_X;
	protected int energyBarY = ExtendedBlockEntityMenuLayout.ENERGY_BAR_Y;
	
	public ExtendedBlockEntityScreenHandler(Supplier<? extends MenuType<?>> type, int syncId, Player player, BlockPos position) {
		super(type, syncId, player, position);
		
		this.blockEntity = (ExtendedBlockEntity) player.level().getBlockEntity(position);
		this.progressData = new ProgressContainerData(blockEntity);
		addDataSlots(progressData);
		
		if (!player.level().isClientSide && blockEntity != null) {
			blockEntity.setSyncItemStorage(true);
			blockEntity.setSyncFluidStorage(true);
			blockEntity.syncData();
		}
		
		addPlayerInventory(player.getInventory(), getPlayerInventoryX(), getPlayerInventoryY());
	}
	
	public ExtendedBlockEntity getBlockEntity() {
		return blockEntity;
	}
	
	public List<GuiArrow> getArrows() {
		return arrows;
	}
	
	public List<GuiFluidBar> getFluidBars() {
		return fluidBars;
	}
	
	public List<GuiFluidFilter> getFluidFilters() {
		return fluidFilters;
	}
	
	public List<GuiItemHint> getItemHints() {
		return itemHints;
	}
	
	public int getEnergyBarX() {
		return energyBarX;
	}
	
	public int getEnergyBarY() {
		return energyBarY;
	}
	
	public int getImageWidth() {
		return ExtendedBlockEntityMenuLayout.imageWidth();
	}
	
	public int getImageHeight() {
		return ExtendedBlockEntityMenuLayout.imageHeight(blockEntity);
	}
	
	public int getInventoryLabelY() {
		return ExtendedBlockEntityMenuLayout.inventoryLabelY(blockEntity);
	}
	
	public int getDefaultFluidSlotForBar() {
		return 0;
	}
	
	public double getProgressRatio() {
		return progressData.getProgressRatio();
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return MenuQuickMove.moveWithPlayerSlotsFirst(this, player, index, this::moveItemStackTo);
	}

	protected void addBlockEntitySlot(int slot, int x, int y) {
		addBlockEntitySlot(slot, x, y, ($) -> true);
	}
	
	protected void addBlockEntitySlot(int slot, int x, int y, Predicate<ItemStack> insertPredicate) {
		if (blockEntity != null && blockEntity.hasItemStorage()) {
			addSlot(new SlotItemHandler(blockEntity.getItemStorage(), slot, x + 1, y + 1) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return super.mayPlace(stack) && insertPredicate.test(stack);
				}
			});
		}
	}
	
	protected void addBlockEntityWildSlot(int slot, int x, int y, Predicate<ItemStack> insertPredicate) {
		if (blockEntity != null && blockEntity.hasItemStorage()) {
			addSlot(new SlotItemHandler(blockEntity.getItemStorage().getWildProxy(), slot, x + 1, y + 1) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return super.mayPlace(stack) && insertPredicate.test(stack);
				}
			});
		}
	}
	
	protected void addBlockEntityOutputSlot(int slot, int x, int y) {
		if (blockEntity != null && blockEntity.hasItemStorage()) {
			addSlot(new SlotItemHandler(blockEntity.getItemStorage(), slot, x + 1, y + 1) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return false;
				}
			});
		}
	}
	
	protected void addBlockEntityWildOutputSlot(int slot, int x, int y) {
		if (blockEntity != null && blockEntity.hasItemStorage()) {
			addSlot(new SlotItemHandler(blockEntity.getItemStorage().getWildProxy(), slot, x + 1, y + 1) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return false;
				}
			});
		}
	}
	
	protected void addProgressArrow(int x, int y) {
		arrows.add(new GuiArrow(x, y, (int) ExtendedBlockEntityMenuLayout.ARROW_WIDTH, (int) ExtendedBlockEntityMenuLayout.ARROW_HEIGHT, true));
	}
	
	protected void addStaticArrow(int x, int y) {
		arrows.add(new GuiArrow(x, y, (int) ExtendedBlockEntityMenuLayout.ARROW_WIDTH, (int) ExtendedBlockEntityMenuLayout.ARROW_HEIGHT, false));
	}
	
	protected void addFluidBar(int tank, int x, int y) {
		fluidBars.add(new GuiFluidBar(tank, x, y, (int) ExtendedBlockEntityMenuLayout.BAR_WIDTH, (int) ExtendedBlockEntityMenuLayout.BAR_HEIGHT));
	}
	
	protected void addFluidFilter(int x, int y) {
		fluidFilters.add(new GuiFluidFilter(x, y, (int) ExtendedBlockEntityMenuLayout.FILTER_WIDTH, (int) ExtendedBlockEntityMenuLayout.FILTER_HEIGHT));
	}
	
	protected void addItemHint(ItemStack stack, int x, int y, Component tooltip) {
		itemHints.add(new GuiItemHint(stack, x, y, (int) ExtendedBlockEntityMenuLayout.SLOT_WIDTH, (int) ExtendedBlockEntityMenuLayout.SLOT_HEIGHT, tooltip));
	}
	
	protected int getPlayerInventoryX() {
		return ExtendedBlockEntityMenuLayout.playerInventoryX();
	}
	
	protected int getPlayerInventoryY() {
		return ExtendedBlockEntityMenuLayout.playerInventoryY(blockEntity);
	}
	
	protected void setEnergyBarPosition(int x, int y) {
		energyBarX = x;
		energyBarY = y;
	}
	
	protected int defaultFluidBarX() {
		return ExtendedBlockEntityMenuLayout.defaultFluidBarX(blockEntity);
	}
	
	protected int defaultFluidBarY() {
		return ExtendedBlockEntityMenuLayout.defaultFluidBarY();
	}
	
	protected void addDefaultFluidBar() {
		addFluidBar(getDefaultFluidSlotForBar(), defaultFluidBarX(), defaultFluidBarY());
	}
	
	protected int fluidArrowX(int fluidBarX) {
		return ExtendedBlockEntityMenuLayout.fluidArrowX(fluidBarX);
	}
	
	protected int fluidArrowY(int fluidBarY) {
		return ExtendedBlockEntityMenuLayout.fluidArrowY(fluidBarY);
	}
	
	protected int centeredProcessInputX() {
		return ExtendedBlockEntityMenuLayout.centeredProcessInputX();
	}
	
	protected int centeredProcessInputY() {
		return ExtendedBlockEntityMenuLayout.centeredProcessInputY();
	}
	
	protected int processArrowX(int inputX) {
		return ExtendedBlockEntityMenuLayout.processArrowX(inputX);
	}
	
	protected int processArrowY(int inputY) {
		return ExtendedBlockEntityMenuLayout.processArrowY(inputY);
	}
	
	protected int processOutputX(int arrowX) {
		return ExtendedBlockEntityMenuLayout.processOutputX(arrowX);
	}
	
	protected int processOutputY(int arrowY) {
		return ExtendedBlockEntityMenuLayout.processOutputY(arrowY);
	}
	
	public record GuiArrow(int x, int y, int width, int height, boolean progress) {
	}
	
	public record GuiFluidBar(int tank, int x, int y, int width, int height) {
	}
	
	public record GuiFluidFilter(int x, int y, int width, int height) {
	}
	
	public record GuiItemHint(ItemStack stack, int x, int y, int width, int height, Component tooltip) {
	}

	private static final class ProgressContainerData implements ContainerData {
		private final ExtendedBlockEntity blockEntity;
		private int progress;

		private ProgressContainerData(ExtendedBlockEntity blockEntity) {
			this.blockEntity = blockEntity;
		}

		@Override
		public int get(int index) {
			if (index != 0) {
				return 0;
			}

			if (blockEntity != null && blockEntity.getLevel() != null && !blockEntity.getLevel().isClientSide()) {
				if (blockEntity.limit <= 0.0D) {
					return 0;
				}

				return Mth.clamp((int) Math.round(blockEntity.progress / blockEntity.limit * PROGRESS_DATA_SCALE), 0, PROGRESS_DATA_SCALE);
			}

			return progress;
		}

		@Override
		public void set(int index, int value) {
			if (index == 0) {
				progress = Mth.clamp(value, 0, PROGRESS_DATA_SCALE);
			}
		}

		@Override
		public int getCount() {
			return 1;
		}

		private double getProgressRatio() {
			if (blockEntity != null && blockEntity.getLevel() != null && blockEntity.getLevel().isClientSide()) {
				if (blockEntity.limit <= 0.0D) {
					return 0.0D;
				}

				return Mth.clamp(blockEntity.progress / blockEntity.limit, 0.0D, 1.0D);
			}

			return get(0) / (double) PROGRESS_DATA_SCALE;
		}
	}
}
