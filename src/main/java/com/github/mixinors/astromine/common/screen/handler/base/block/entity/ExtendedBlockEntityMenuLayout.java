/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.common.screen.handler.base.block.entity;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.block.entity.rocket.RocketControllerBlockEntity;
import com.github.mixinors.astromine.common.block.entity.storage.BufferBlockEntity;

public final class ExtendedBlockEntityMenuLayout {
	public static final float SLOT_WIDTH = 18.0F;
	public static final float SLOT_HEIGHT = 18.0F;
	public static final float BAR_HEIGHT = 48.0F;
	public static final float BAR_WIDTH = 24.0F;
	public static final float REDSTONE_WIDTH = 20.0F;
	public static final float REDSTONE_HEIGHT = 19.0F;
	public static final float TABS_WIDTH = 176.0F;
	public static final float TABS_HEIGHT = 188.0F;
	public static final float ARROW_WIDTH = 22.0F;
	public static final float ARROW_HEIGHT = 16.0F;
	public static final float FILTER_WIDTH = 8.0F;
	public static final float FILTER_HEIGHT = 8.0F;
	public static final float LAUNCH_BUTTON_WIDTH = 44.0F;
	public static final float LAUNCH_BUTTON_HEIGHT = 44.0F;
	public static final float CLEAR_BUTTON_WIDTH = 32.0F;
	public static final float CLEAR_BUTTON_HEIGHT = 18.0F;
	public static final float PAD_2 = 2.0F;
	public static final float PAD_3 = 3.0F;
	public static final float PAD_4 = 4.0F;
	public static final float PAD_5 = 5.0F;
	public static final float PAD_7 = 7.0F;
	public static final float PAD_8 = 8.0F;
	public static final float PAD_10 = 10.0F;
	public static final float PAD_11 = 11.0F;
	public static final float PAD_25 = 25.0F;
	public static final float PAD_38 = 38.0F;
	public static final float PAD_68 = 68.0F;
	
	public static final int ENERGY_BAR_X = 7;
	public static final int ENERGY_BAR_Y = 43;
	public static final int FIRST_FLUID_BAR_X = 38;
	public static final int FIRST_FLUID_BAR_Y = 43;
	public static final int PLAYER_INVENTORY_X = 7;
	public static final int PLAYER_INVENTORY_Y = 105;
	
	private ExtendedBlockEntityMenuLayout() {
	}
	
	public static int imageWidth() {
		return (int) TABS_WIDTH;
	}
	
	public static int imageHeight(ExtendedBlockEntity blockEntity) {
		if (blockEntity instanceof BufferBlockEntity && !(blockEntity instanceof BufferBlockEntity.Creative)) {
			return (int) (TABS_HEIGHT + 58.0F);
		}
		
		if (blockEntity instanceof RocketControllerBlockEntity) {
			return (int) (TABS_HEIGHT + 81.0F);
		}
		
		return (int) TABS_HEIGHT;
	}
	
	public static int inventoryLabelY(ExtendedBlockEntity blockEntity) {
		return playerInventoryY(blockEntity) - 10;
	}
	
	public static int playerInventoryX() {
		return PLAYER_INVENTORY_X;
	}
	
	public static int playerInventoryY(ExtendedBlockEntity blockEntity) {
		if (blockEntity instanceof BufferBlockEntity && !(blockEntity instanceof BufferBlockEntity.Creative)) {
			return PLAYER_INVENTORY_Y + 58;
		}
		
		if (blockEntity instanceof RocketControllerBlockEntity) {
			return PLAYER_INVENTORY_Y + 81;
		}
		
		return PLAYER_INVENTORY_Y;
	}
	
	public static int defaultFluidBarX(ExtendedBlockEntity blockEntity) {
		return blockEntity != null && blockEntity.hasEnergyStorage() ? FIRST_FLUID_BAR_X : ENERGY_BAR_X;
	}
	
	public static int defaultFluidBarY() {
		return FIRST_FLUID_BAR_Y;
	}
	
	public static int fluidArrowX(int fluidBarX) {
		return fluidBarX + (int) (BAR_WIDTH + PAD_7);
	}
	
	public static int fluidArrowY(int fluidBarY) {
		return fluidBarY + (int) (BAR_HEIGHT / 2.0F - PAD_8);
	}
	
	public static int centeredProcessInputX() {
		return ENERGY_BAR_X + (int) (TABS_WIDTH / 2.0F - (SLOT_WIDTH + PAD_7 + ARROW_WIDTH + PAD_7 + SLOT_WIDTH) / 2.0F - SLOT_WIDTH / 2.0F);
	}
	
	public static int centeredProcessInputY() {
		return ENERGY_BAR_Y + (int) (BAR_HEIGHT / 2.0F - SLOT_HEIGHT / 2.0F);
	}
	
	public static int processArrowX(int inputX) {
		return inputX + (int) (SLOT_WIDTH + PAD_7);
	}
	
	public static int processArrowY(int inputY) {
		return inputY + (int) ((SLOT_HEIGHT - ARROW_HEIGHT) / 2.0F - 0.5F);
	}
	
	public static int processOutputX(int arrowX) {
		return arrowX + (int) (ARROW_WIDTH + PAD_7);
	}
	
	public static int processOutputY(int arrowY) {
		return arrowY + (int) ((ARROW_HEIGHT - SLOT_HEIGHT) / 2.0F + 1.0F);
	}
}
