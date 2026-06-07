/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.screen.base;

import com.github.mixinors.astromine.client.gui.widget.TabPage;
import com.github.mixinors.astromine.client.gui.widget.TabWidget;
import com.github.mixinors.astromine.client.gui.widget.WidgetContext;
import com.github.mixinors.astromine.client.gui.widget.WidgetGroup;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.BlockStateScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.transfer.StorageType;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class CustomForegroundBaseHandledScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
	protected final Inventory playerInventory;
	protected WidgetGroup root;
	protected TabWidget tabs;
	private final Map<TabPage, StorageType> tabStorageTypes = new IdentityHashMap<>();
	
	public CustomForegroundBaseHandledScreen(@NotNull T handler, @NotNull Inventory inventory, @NotNull Component title) {
		super(handler, inventory, title);
		this.playerInventory = inventory;
		
		if (handler instanceof ExtendedBlockEntityScreenHandler blockEntityHandler) {
			imageWidth = blockEntityHandler.getImageWidth();
			imageHeight = blockEntityHandler.getImageHeight();
			inventoryLabelY = blockEntityHandler.getInventoryLabelY();
		} else {
			imageWidth = 176;
			imageHeight = 188;
			inventoryLabelY = 87;
		}
		
		titleLabelX = 8;
		titleLabelY = 32;
		inventoryLabelX = 7;
	}
	
	@Override
	protected void init() {
		super.init();
		rebuildWidgets();
	}
	
	protected void rebuildWidgets() {
		root = new WidgetGroup(0, 0, imageWidth, imageHeight);
		tabStorageTypes.clear();
		tabs = BlockEntityScreenWidgets.build(this, root, tabStorageTypes);
	}
	
	protected WidgetContext widgetContext(int mouseX, int mouseY) {
		return new WidgetContext(leftPos, topPos, mouseX, mouseY, font);
	}
	
	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		if (root != null) {
			root.render(graphics, widgetContext(mouseX, mouseY));
		}
	}
	
	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
	}
	
	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(graphics, mouseX, mouseY, partialTick);
		super.render(graphics, mouseX, mouseY, partialTick);
		
		if (root != null) {
			root.renderTooltip(graphics, widgetContext(mouseX, mouseY));
		}
		
		renderTooltip(graphics, mouseX, mouseY);
	}
	
	@Override
	protected void renderSlot(GuiGraphics graphics, Slot slot) {
		if (shouldRenderSlot(slot)) {
			super.renderSlot(graphics, slot);
		}
	}
	
	protected boolean shouldRenderSlot(Slot slot) {
		return selectedStorageTab() == null || slot.container == playerInventory;
	}
	
	@Override
	protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
		if (selectedStorageTab() != null && isHiddenSlotBounds(x, y, width, height)) {
			return false;
		}
		
		return super.isHovering(x, y, width, height, mouseX, mouseY);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (root != null && root.mouseClicked(widgetContext((int) mouseX, (int) mouseY), mouseX, mouseY, button)) {
			return true;
		}
		
		if (selectedStorageTab() != null && hitsHiddenSlot(mouseX, mouseY)) {
			return true;
		}
		
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	private boolean isHiddenSlotBounds(int x, int y, int width, int height) {
		if (width != 16 || height != 16) {
			return false;
		}
		
		for (var slot : menu.slots) {
			if (!shouldRenderSlot(slot) && slot.x == x && slot.y == y) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean hitsHiddenSlot(double mouseX, double mouseY) {
		for (var slot : menu.slots) {
			if (shouldRenderSlot(slot)) {
				continue;
			}
			
			if (mouseX >= leftPos + slot.x && mouseX < leftPos + slot.x + 16 && mouseY >= topPos + slot.y && mouseY < topPos + slot.y + 16) {
				return true;
			}
		}
		
		return false;
	}
	
	private StorageType selectedStorageTab() {
		return tabs == null ? null : tabStorageTypes.get(tabs.selectedPage());
	}
	
	AbstractContainerMenu menu() {
		return menu;
	}
	
	Component screenTitle() {
		return title;
	}
	
	Component inventoryTitle() {
		return playerInventoryTitle;
	}
	
	int imageWidth() {
		return imageWidth;
	}
	
	int imageHeight() {
		return imageHeight;
	}
	
	int inventoryLabelY() {
		return inventoryLabelY;
	}
	
	ExtendedBlockEntity blockEntity() {
		return menu instanceof ExtendedBlockEntityScreenHandler handler ? handler.getBlockEntity() : null;
	}
	
	ExtendedBlockEntityScreenHandler blockEntityHandler() {
		return menu instanceof ExtendedBlockEntityScreenHandler handler ? handler : null;
	}
	
	BlockPos blockPos() {
		return menu instanceof BlockStateScreenHandler handler ? handler.getBlockPos() : BlockPos.ZERO;
	}
}
