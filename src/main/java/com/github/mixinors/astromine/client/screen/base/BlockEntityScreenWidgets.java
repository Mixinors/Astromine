/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.screen.base;

import com.github.mixinors.astromine.client.gui.widget.ArrowWidget;
import com.github.mixinors.astromine.client.gui.widget.ButtonWidget;
import com.github.mixinors.astromine.client.gui.widget.EnergyBarWidget;
import com.github.mixinors.astromine.client.gui.widget.FluidBarWidget;
import com.github.mixinors.astromine.client.gui.widget.ItemWidget;
import com.github.mixinors.astromine.client.gui.widget.SlotBackgroundsWidget;
import com.github.mixinors.astromine.client.gui.widget.TabPage;
import com.github.mixinors.astromine.client.gui.widget.TabWidget;
import com.github.mixinors.astromine.client.gui.widget.TextWidget;
import com.github.mixinors.astromine.client.gui.widget.WidgetGroup;
import com.github.mixinors.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.block.entity.storage.BufferBlockEntity;
import com.github.mixinors.astromine.common.block.entity.storage.TankBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityMenuLayout;
import com.github.mixinors.astromine.common.transfer.StorageType;
import com.github.mixinors.astromine.common.util.MirrorUtils;
import com.github.mixinors.astromine.registry.common.AMNetworking;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;

public final class BlockEntityScreenWidgets {
	private BlockEntityScreenWidgets() {
	}
	
	public static TabWidget build(CustomForegroundBaseHandledScreen<?> screen, WidgetGroup root, Map<TabPage, StorageType> tabStorageTypes) {
		var blockEntity = screen.blockEntity();
		
		if (blockEntity == null) {
			return null;
		}
		
		var tabs = root.add(new TabWidget(0, 0, screen.imageWidth(), screen.imageHeight()));
		var mainTab = tabs.addTab(() -> blockEntity.getBlockState().getBlock().asItem().getDefaultInstance(), () -> blockEntity.getBlockState().getBlock().getName());
		tabStorageTypes.put(mainTab, null);
		populateMainTab(screen, mainTab, blockEntity);
		
		if (blockEntity.hasItemStorage()) {
			addStorageTab(screen, tabs, tabStorageTypes, blockEntity, StorageType.ITEM);
		}
		
		if (blockEntity.hasFluidStorage()) {
			addStorageTab(screen, tabs, tabStorageTypes, blockEntity, StorageType.FLUID);
		}
		
		root.add(new RedstoneControlWidget(screen.imageWidth() - (int) ExtendedBlockEntityMenuLayout.REDSTONE_WIDTH, 0, blockEntity, screen::blockPos));
		return tabs;
	}
	
	private static void populateMainTab(CustomForegroundBaseHandledScreen<?> screen, TabPage tab, ExtendedBlockEntity blockEntity) {
		var handler = screen.blockEntityHandler();
		tab.add(new TextWidget(8, 32, screen::screenTitle));
		tab.add(new TextWidget(7, screen.inventoryLabelY(), screen::inventoryTitle));
		tab.add(new SlotBackgroundsWidget(screen.menu(), screen::shouldRenderSlot));
		
		if (blockEntity.hasEnergyStorage()) {
			var x = handler == null ? ExtendedBlockEntityMenuLayout.ENERGY_BAR_X : handler.getEnergyBarX();
			var y = handler == null ? ExtendedBlockEntityMenuLayout.ENERGY_BAR_Y : handler.getEnergyBarY();
			tab.add(new EnergyBarWidget(x, y, blockEntity::getEnergyStorage));
		}
		
		if (blockEntity.hasFluidStorage()) {
			if (handler != null && !handler.getFluidBars().isEmpty()) {
				for (var bar : handler.getFluidBars()) {
					var storage = blockEntity.getFluidStorage();
					tab.add(new FluidBarWidget(bar.x(), bar.y(), () -> bar.tank() < storage.getTanks() ? storage.getFluidInTank(bar.tank()) : FluidStack.EMPTY, () -> bar.tank() < storage.getTanks() ? storage.getTankCapacity(bar.tank()) : 1));
				}
			} else {
				var storage = blockEntity.getFluidStorage();
				var tank = handler == null ? 0 : handler.getDefaultFluidSlotForBar();
				var x = blockEntity.hasEnergyStorage() ? ExtendedBlockEntityMenuLayout.FIRST_FLUID_BAR_X : ExtendedBlockEntityMenuLayout.ENERGY_BAR_X;
				tab.add(new FluidBarWidget(x, ExtendedBlockEntityMenuLayout.FIRST_FLUID_BAR_Y, () -> tank < storage.getTanks() ? storage.getFluidInTank(tank) : FluidStack.EMPTY, () -> tank < storage.getTanks() ? storage.getTankCapacity(tank) : 1));
			}
		}
		
		if (handler == null) {
			return;
		}
		
		for (var arrow : handler.getArrows()) {
			tab.add(new ArrowWidget(arrow.x(), arrow.y(), () -> arrow.progress() ? handler.getProgressRatio() : 1.0D));
		}
		
		if (blockEntity instanceof TankBlockEntity tank) {
			for (var filter : handler.getFluidFilters()) {
				tab.add(new TankFluidFilterWidget(filter.x(), filter.y(), screen.menu(), tank, screen::blockPos));
			}
		}
		
		if (blockEntity instanceof BufferBlockEntity.Creative buffer) {
			tab.add(new ButtonWidget(72, 71, (int) ExtendedBlockEntityMenuLayout.CLEAR_BUTTON_WIDTH, (int) ExtendedBlockEntityMenuLayout.CLEAR_BUTTON_HEIGHT, () -> Component.translatable("text.astromine.clear"), () -> true, () -> {
				if (buffer.getItemStorage() != null) {
					buffer.getItemStorage().setItem(0, ItemStack.EMPTY);
				}
				
				PacketDistributor.sendToServer(new AMNetworking.BufferClearPayload(screen.blockPos()));
			}));
		}
		
		for (var hint : handler.getItemHints()) {
			tab.add(new ItemWidget(hint.x(), hint.y(), hint::stack, () -> List.of(hint.tooltip())));
		}
	}
	
	private static void addStorageTab(CustomForegroundBaseHandledScreen<?> screen, TabWidget tabs, Map<TabPage, StorageType> tabStorageTypes, ExtendedBlockEntity blockEntity, StorageType storageType) {
		var tab = tabs.addTab(() -> storageType.getItem().getDefaultInstance(), storageType::getName);
		tabStorageTypes.put(tab, storageType);
		tab.add(new TextWidget(8, 32, storageType::getName));
		tab.add(new TextWidget(7, screen.inventoryLabelY(), screen::inventoryTitle));
		tab.add(new SlotBackgroundsWidget(screen.menu(), screen::shouldRenderSlot));
		
		var facing = blockEntityFacing(blockEntity);
		
		for (var direction : Direction.values()) {
			var point = sidingPoint(direction, facing);
			tab.add(new StorageSidingWidget(point.x(), point.y(), blockEntity, storageType, direction, facing, screen::blockPos));
		}
	}
	
	private static Direction blockEntityFacing(ExtendedBlockEntity blockEntity) {
		var state = blockEntity.getBlockState();
		var block = state.getBlock();
		
		if (block instanceof HorizontalFacingBlockWithEntity facingBlock && state.hasProperty(facingBlock.getDirectionProperty())) {
			return state.getValue(facingBlock.getDirectionProperty());
		}
		
		return Direction.NORTH;
	}
	
	private static SidingPoint sidingPoint(Direction direction, Direction rotation) {
		var offset = MirrorUtils.rotate(direction, rotation);
		var anchorX = (int) (ExtendedBlockEntityMenuLayout.TABS_WIDTH / 2.0F - ExtendedBlockEntityMenuLayout.PAD_38);
		
		return switch (offset) {
			case NORTH -> new SidingPoint(anchorX + 29, 53);
			case SOUTH -> new SidingPoint(anchorX + 7, 75);
			case UP -> new SidingPoint(anchorX + 29, 31);
			case DOWN -> new SidingPoint(anchorX + 29, 75);
			case WEST -> new SidingPoint(anchorX + 51, 53);
			case EAST -> new SidingPoint(anchorX + 7, 53);
		};
	}
	
	private record SidingPoint(int x, int y) {
	}
}
