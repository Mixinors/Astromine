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

package com.github.mixinors.astromine.common.screenhandler.base.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;

import com.github.mixinors.astromine.common.entity.base.ExtendedEntity;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.api.common.util.SlotUtils;
import dev.vini2003.hammer.gui.api.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import dev.vini2003.hammer.gui.api.common.widget.tab.TabWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextWidget;


import dev.vini2003.hammer.gui.energy.api.common.widget.bar.EnergyBarWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;

public abstract class ExtendedEntityScreenHandler extends BaseScreenHandler {
	protected final ExtendedEntity entity;

	protected EnergyBarWidget energyBar = null;
	protected FluidBarWidget fluidBar = null;
	
	protected Collection<SlotWidget> playerSlots = new HashSet<>();

	protected TabWidget tabs;

	protected TabWidget.TabWidgetCollection mainTab;
	
	public ExtendedEntityScreenHandler(Supplier<? extends ScreenHandlerType<?>> type, int syncId, PlayerEntity player, int entityId) {
		super(type.get(), syncId, player);

		entity = (ExtendedEntity) player.world.getEntityById(entityId);
	}
	
	@Override
	public boolean isClient() {
		return getClient();
	}
	
	public abstract ItemStack getSymbol();
	
	public int getTabWidgetExtendedHeight() {
		return 0;
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return this.entity.isAlive() && this.entity.distanceTo(player) < 8.0F;
	}
	
	@Override
	public void initialize(int width, int height) {
		tabs = new TabWidget();
		tabs.setSize( new Size(176.0F, 188F + getTabWidgetExtendedHeight(), 0.0F));
		tabs.setPosition( new Position(width / 2.0F - tabs.getWidth() / 2.0F, height / 2.0F - tabs.getHeight() / 2.0F, 0.0F));

		add(tabs);

		mainTab = (TabWidget.TabWidgetCollection) tabs.addTab(getSymbol());
		mainTab.setPosition(new Position(tabs, 0.0F, 25.0F + 7.0F, 0.0F));
		mainTab.setSize(new Size(176.0F, 184.0F, 0.0F));
		
		var title = new TextWidget();
		title.setPosition(new Position(mainTab, 8.0F, 0.0F, 0.0F));
		title.setText(entity.getDisplayName());
		title.setColor(4210752);
		
		mainTab.add(title);
		
		var invPos = new Position(tabs, 7.0F, 25.0F + 7.0F + (184.0F - 18.0F - 18.0F - (18.0F * 4.0F) - 3.0F + getTabWidgetExtendedHeight()), 0.0F);
		
		var invTitle = new TextWidget();
		invTitle.setPosition(new Position(invPos, 0.0F, -10.0F, 0.0F));
		invTitle.setText(getPlayer().getInventory().getName());
		invTitle.setColor(4210752);
		
		mainTab.add(invTitle);
		
		playerSlots = SlotUtils.addPlayerInventory(invPos, new Size(18.0F, 18.0F, 0.0F), mainTab, getPlayer().getInventory());
		
		if (entity.hasEnergyStorage()) {
			energyBar = new EnergyBarWidget();
			energyBar.setPosition( new Position(mainTab, 7.0F, 11.0F, 0.0F));
			energyBar.setSize( new Size(24.0F, 48.0F, 0.0F));
			energyBar.setStorage(entity.getEnergyStorage());
			energyBar.setCurrent(() -> (float) entity.getEnergyStorage().getAmount());
			energyBar.setMaximum(() -> (float) entity.getEnergyStorage().getCapacity());
			
			mainTab.add(energyBar);
		}
		
		if (entity.hasFluidStorage()) {
			fluidBar = new FluidBarWidget();
			
			if (energyBar == null) {
				fluidBar.setPosition(new Position(mainTab, 7.0F, 0.0F, 0.0F));
			} else {
				fluidBar.setPosition(new Position(energyBar, 7.0F, 0.0F, 0.0F));
			}
			
			fluidBar.setSize(new Size(24.0F, 48.0F, 0.0F));
			fluidBar.setStorage(entity.getFluidStorage().getStorage(0));
		}
	}
}
