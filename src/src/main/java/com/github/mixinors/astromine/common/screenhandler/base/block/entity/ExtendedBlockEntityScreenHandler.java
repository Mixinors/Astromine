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

package com.github.mixinors.astromine.common.screenhandler.base.block.entity;

import com.github.mixinors.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.screenhandler.base.block.BlockStateScreenHandler;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import com.github.mixinors.astromine.common.util.WidgetUtils;
import com.github.mixinors.astromine.common.widget.RedstoneControlWidget;
import dev.architectury.hooks.block.BlockEntityHooks;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.util.SlotUtils;
import dev.vini2003.hammer.gui.api.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import dev.vini2003.hammer.gui.api.common.widget.tab.TabWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextWidget;
import dev.vini2003.hammer.gui.energy.api.common.widget.bar.EnergyBarWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class ExtendedBlockEntityScreenHandler extends BlockStateScreenHandler {
	protected final ExtendedBlockEntity blockEntity;
	
	protected EnergyBarWidget energyBar = null;
	protected FluidBarWidget fluidBar = null;
	
	protected Collection<SlotWidget> playerSlots = new HashSet<>();
	
	protected TabWidget tabs;
	
	protected TabWidget.TabWidgetCollection mainTab;
	
	public ExtendedBlockEntityScreenHandler(Supplier<? extends ScreenHandlerType<?>> type, int syncId, PlayerEntity player, BlockPos position) {
		super(type, syncId, player, position);
		
		this.blockEntity = (ExtendedBlockEntity) player.world.getBlockEntity(position);
		
		if (!player.world.isClient) {
			blockEntity.setSyncItemStorage(true);
			blockEntity.setSyncFluidStorage(true);
			
			BlockEntityHooks.syncData(blockEntity);
		}
	}
	
	public ExtendedBlockEntity getBlockEntity() {
		return blockEntity;
	}
	
	public Position getTabsPosition(int width, int height) {
		return new Position(width / 2 - tabs.getWidth() / 2, height / 2 - tabs.getHeight() / 2, 0.0F);
	}
	
	public Size getTabsSize(int width, int height) {
		return new Size(176.0F, 188.0F + getTabWidgetExtendedHeight(), 0.0F);
	}
	
	public int getTabWidgetExtendedHeight() {
		return 0;
	}
	
	@Override
	public void initialize(int width, int height) {
		tabs = new TabWidget();
		tabs.setSize(getTabsSize(width, height));
		tabs.setPosition(getTabsPosition(width, height));
		
		add(tabs);
		
		mainTab = (TabWidget.TabWidgetCollection) tabs.addTab(blockEntity.getCachedState().getBlock().asItem(), () -> Collections.singletonList(new TranslatableText(blockEntity.getCachedState().getBlock().getTranslationKey())));
		mainTab.setPosition(new Position(tabs, 0, 25.0F + 7.0F, 0.0F));
		mainTab.setSize(new Size(176.0F, 184.0F, 0.0F));
		
		var title = new TextWidget();
		title.setPosition(new Position(mainTab, 8, 0, 0.0F));
		title.setText(new TranslatableText(blockEntity.getCachedState().getBlock().asItem().getTranslationKey()));
		title.setColor(4210752);
		mainTab.add(title);
		
		var invPos = new Position(tabs,
				7.0F + (tabs.getWidth() / 2.0F - ((9.0F * 18.0F) / 2.0F) - 7.0F),
				25.0F + 7.0F + (184.0F - 18.0F - 18.0F - (18.0F * 4.0F) - 3.0F + getTabWidgetExtendedHeight()),
				0.0F
		);
		
		var invTitle = new TextWidget();
		invTitle.setPosition(new Position(invPos, 0.0F, -10.0F, 0.0F));
		invTitle.setText(getPlayer().getInventory().getName());
		invTitle.setColor(4210752);
		mainTab.add(invTitle);
		playerSlots = SlotUtils.addPlayerInventory(invPos, new Size(18.0F, 18.0F, 0.0F), mainTab, getPlayer().getInventory());
		
		var rotation = new Direction[] { Direction.NORTH };
		var block = blockEntity.getCachedState().getBlock();
		
		if (block instanceof HorizontalFacingBlockWithEntity) {
			var property = ((HorizontalFacingBlockWithEntity) block).getDirectionProperty();
			
			if (property != null) {
				rotation[0] = blockEntity.getCachedState().get(property);
			}
		}
		
		var redstoneWidget = new RedstoneControlWidget();
		redstoneWidget.setPosition(new Position(tabs, tabs.getWidth() - 20.0F, 0.0F, 0.0F));
		redstoneWidget.setSize(new Size(20.0F, 19.0F, 0.0F));
		redstoneWidget.setBlockEntity(blockEntity);
		
		add(redstoneWidget);
		
		var tabAdder = (BiConsumer<StorageSiding[], StorageType>) (sidings, type) -> {
			var tabCollection = (TabWidget.TabWidgetCollection) tabs.addTab(type.getItem(), () -> Collections.singletonList(type.getName()));
			
			WidgetUtils.createStorageSiding(
					new Position(tabs, tabs.getWidth() / 2.0F - 38.0F, getTabWidgetExtendedHeight() / 2.0F, 0.0F), blockEntity, sidings, type, rotation[0]
			).forEach(tabCollection::add);
			
			var invTabTitle = new TextWidget();
			invTabTitle.setPosition(new Position(invPos, 0.0F, -10.0F, 0.0F));
			invTabTitle.setText(getPlayer().getInventory().getName());
			invTabTitle.setColor(4210752);
			
			tabCollection.add(invTabTitle);
			
			playerSlots.addAll(SlotUtils.addPlayerInventory(invPos, new Size(18.0F, 18.0F, 0.0F), tabCollection, getPlayer().getInventory()));
			
			var tabTitle = new TextWidget();
			tabTitle.setPosition(new Position(mainTab, 8.0F, 0.0F, 0.0F));
			tabTitle.setText(type.getName());
			tabTitle.setColor(4210752);
			
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
			energyBar.setPosition(new Position(mainTab, 7.0F, 11.0F, 0.0F));
			energyBar.setSize(new Size(24.0F, 48.0F, 0.0F));
			energyBar.setStorage(blockEntity.getEnergyStorage());
			energyBar.setSmooth(false);
			energyBar.setCurrent(() -> (float) blockEntity.getEnergyStorage().getAmount());
			energyBar.setMaximum(() -> (float) blockEntity.getEnergyStorage().getCapacity());
			
			mainTab.add(energyBar);
		}
		
		if (blockEntity.hasFluidStorage()) {
			fluidBar = new FluidBarWidget();
			
			if (energyBar == null) {
				fluidBar.setPosition(new Position(mainTab, 7.0F, 11.0F, 0.0F));
			} else {
				fluidBar.setPosition(new Position(energyBar, energyBar.getWidth() + 7.0F, 0.0F, 0.0F));
			}
			
			fluidBar.setSize(new Size(24.0F, 48.0F, 0.0F));
			fluidBar.setStorage(blockEntity.getFluidStorage().getStorage(0));
			fluidBar.setSmooth(false);
			
			mainTab.add(fluidBar);
		}
	}
}
