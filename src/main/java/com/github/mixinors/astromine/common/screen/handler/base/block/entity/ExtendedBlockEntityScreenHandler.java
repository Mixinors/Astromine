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

package com.github.mixinors.astromine.common.screen.handler.base.block.entity;

import com.github.mixinors.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.BlockStateScreenHandler;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import com.github.mixinors.astromine.common.util.WidgetUtils;
import com.github.mixinors.astromine.common.widget.RedstoneControlWidget;
import com.google.common.collect.ImmutableList;
import dev.architectury.hooks.block.BlockEntityHooks;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.util.SlotUtil;
import dev.vini2003.hammer.gui.api.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import dev.vini2003.hammer.gui.api.common.widget.tab.TabWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextWidget;
import dev.vini2003.hammer.gui.energy.api.common.widget.bar.EnergyBarWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class ExtendedBlockEntityScreenHandler extends BlockStateScreenHandler {
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
	
	protected final ExtendedBlockEntity blockEntity;
	
	protected EnergyBarWidget energyBar = null;
	protected FluidBarWidget fluidBar = null;
	
	protected Collection<SlotWidget> playerSlots = new HashSet<>();
	
	protected TabWidget tabs;
	
	protected TabWidget.TabCollection tab;
	
	public ExtendedBlockEntityScreenHandler(Supplier<? extends ScreenHandlerType<?>> type, int syncId, PlayerEntity player, BlockPos position) {
		super(type, syncId, player, position);
		
		this.blockEntity = (ExtendedBlockEntity) player.getWorld().getBlockEntity(position);
		
		if (!player.getWorld().isClient) {
			blockEntity.setSyncItemStorage(true);
			blockEntity.setSyncFluidStorage(true);
			
			BlockEntityHooks.syncData(blockEntity);
		}
	}
	
	public ExtendedBlockEntity getBlockEntity() {
		return blockEntity;
	}
	
	public Size getTabsSizeExtension() {
		return new Size(0.0F, 0.0F);
	}
	
	public int getDefaultFluidSlotForBar() {
		return 0;
	}
	
	@Override
	public void init(int width, int height) {
		var state = blockEntity.getCachedState();
		var block = state.getBlock();
		var symbol = block.asItem().getDefaultStack();
		
		var player = getPlayer();
		var inventory = player.getInventory();
		
		var tabsExtension = getTabsSizeExtension();
		
		tabs = new TabWidget();
		tabs.setSize(new Size(TABS_WIDTH + tabsExtension.getWidth(), TABS_HEIGHT + tabsExtension.getHeight()));
		tabs.setPosition(new Position(width / 2.0F - tabs.getWidth() / 2.0F, height / 2.0F - tabs.getHeight() / 2.0F));
		
		add(tabs);
		
		tab = tabs.addTab(() -> symbol, () -> ImmutableList.of(Text.translatable(block.getTranslationKey())));
		tab.setPosition(new Position(tabs, 0.0F, PAD_25 + PAD_7));
		tab.setSize(new Size(TABS_WIDTH, TABS_HEIGHT));
		
		var title = new TextWidget();
		title.setPosition(new Position(tab, PAD_8, 0.0F));
		title.setText(Text.translatable(block.getTranslationKey()));
		title.setColor(new Color(0x404040));
		
		tab.add(title);
		
		var inventoryPos = new Position(tabs,
				PAD_7 + (tabs.getWidth() / 2.0F - ((9.0F * SLOT_WIDTH) / 2.0F) - PAD_7),
				PAD_8 + PAD_25 + PAD_7 + (TABS_WIDTH - SLOT_WIDTH - SLOT_WIDTH - (SLOT_WIDTH * 4.0F) - PAD_3 + tabsExtension.getHeight())
		);
		
		var inventoryTitle = new TextWidget();
		inventoryTitle.setPosition(new Position(inventoryPos, 0.0F, -PAD_10));
		inventoryTitle.setText(getPlayer().getInventory().getName());
		inventoryTitle.setColor(new Color(0x404040));
		
		tab.add(inventoryTitle);
		
		playerSlots = SlotUtil.addPlayerInventory(inventoryPos, new Size(SLOT_WIDTH, SLOT_HEIGHT), tab, inventory);
		
		var rotation = block instanceof HorizontalFacingBlockWithEntity blockWithEntity ? (state.get(blockWithEntity.getDirectionProperty())) : Direction.NORTH;
		
		var redstoneWidget = new RedstoneControlWidget();
		redstoneWidget.setPosition(new Position(tabs, tabs.getWidth() - REDSTONE_WIDTH, 0.0F));
		redstoneWidget.setSize(new Size(REDSTONE_WIDTH, REDSTONE_HEIGHT));
		redstoneWidget.setBlockEntity(blockEntity);
		
		add(redstoneWidget);
		
		var tabAdder = (BiConsumer<StorageSiding[], StorageType>) (sidings, type) -> {
			var typeSymbol = new ItemStack(type.getItem());
			var tabCollection = (TabWidget.TabCollection) tabs.addTab(() -> typeSymbol, () -> ImmutableList.of(type.getName()));
			
			for (var widget : WidgetUtils.createStorageSiding(new Position(tabs, tabs.getWidth() / 2.0F - PAD_38, tabsExtension.getWidth() / 2.0F), blockEntity, type, rotation)) {
				tabCollection.add(widget);
			}
			
			var invTabTitle = new TextWidget();
			invTabTitle.setPosition(new Position(inventoryPos, 0.0F, -PAD_10));
			invTabTitle.setText(getPlayer().getInventory().getName());
			invTabTitle.setColor(new Color(0x404040));
			
			tabCollection.add(invTabTitle);
			
			playerSlots.addAll(SlotUtil.addPlayerInventory(inventoryPos, new Size(SLOT_WIDTH, SLOT_HEIGHT), tabCollection, inventory));
			
			var tabTitle = new TextWidget();
			tabTitle.setPosition(new Position(tab, PAD_8, 0.0F));
			tabTitle.setText(type.getName());
			tabTitle.setColor(new Color(0x404040));
			
			tabCollection.add(tabTitle);
		};
		
		if (blockEntity.hasItemStorage()) {
			tabAdder.accept(blockEntity.getItemStorage().getSidings(), StorageType.ITEM);
		}
		
		if (blockEntity.hasFluidStorage()) {
			tabAdder.accept(blockEntity.getFluidStorage().getSidings(), StorageType.FLUID);
		}
		
		if (blockEntity.hasEnergyStorage()) {
			energyBar = new EnergyBarWidget();
			energyBar.setPosition(new Position(tab, PAD_7, PAD_11));
			energyBar.setSize(new Size(BAR_WIDTH, BAR_HEIGHT));
			energyBar.setStorage(() -> blockEntity.getEnergyStorage());
			energyBar.setSmooth(false);
			energyBar.setCurrent(() -> (float) blockEntity.getEnergyStorage().getAmount());
			energyBar.setMaximum(() -> (float) blockEntity.getEnergyStorage().getCapacity());
			
			tab.add(energyBar);
		}
		
		if (blockEntity.hasFluidStorage()) {
			fluidBar = new FluidBarWidget();
			
			if (energyBar == null) {
				fluidBar.setPosition(new Position(tab, PAD_7, PAD_11));
			} else {
				fluidBar.setPosition(new Position(energyBar, energyBar.getWidth() + PAD_7, 0.0F));
			}
			
			fluidBar.setSize(new Size(BAR_WIDTH, BAR_HEIGHT));
			fluidBar.setStorageView(() -> blockEntity.getFluidStorage().getStorage(getDefaultFluidSlotForBar()));
			fluidBar.setSmooth(false);
			
			tab.add(fluidBar);
		}
	}
	
	@Override
	public ItemStack quickMove(PlayerEntity player, int index) {
		onSlotClick(index, GLFW.GLFW_MOUSE_BUTTON_1, SlotActionType.QUICK_MOVE, player);
		return ItemStack.EMPTY;
	}
}
