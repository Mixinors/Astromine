/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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
import dev.vini2003.hammer.common.geometry.position.Position;
import dev.vini2003.hammer.common.geometry.size.Size;
import dev.vini2003.hammer.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.common.util.Slots;
import dev.vini2003.hammer.common.widget.bar.EnergyBarWidget;
import dev.vini2003.hammer.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.common.widget.slot.SlotWidget;
import dev.vini2003.hammer.common.widget.tab.TabWidget;
import dev.vini2003.hammer.common.widget.text.TextWidget;

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
		tabs.setSize( Size.of(176.0F, 188F + getTabWidgetExtendedHeight(), 0.0F));
		tabs.setPosition( Position.of(width / 2.0F - tabs.getWidth() / 2.0F, height / 2.0F - tabs.getHeight() / 2.0F, 0.0F));

		add(tabs);

		mainTab = (TabWidget.TabWidgetCollection) tabs.addTab(getSymbol());
		mainTab.setPosition(Position.of(tabs, 0.0F, 25.0F + 7.0F, 0.0F));
		mainTab.setSize(Size.of(176.0F, 184.0F, 0.0F));
		
		var title = new TextWidget();
		title.setPosition(Position.of(mainTab, 8.0F, 0.0F, 0.0F));
		title.setText(entity.getDisplayName());
		title.setColor(4210752);
		
		mainTab.add(title);
		
		var invPos = Position.of(tabs, 7.0F, 25.0F + 7.0F + (184.0F - 18.0F - 18.0F - (18.0F * 4.0F) - 3.0F + getTabWidgetExtendedHeight()), 0.0F);
		
		var invTitle = new TextWidget();
		invTitle.setPosition(Position.of(invPos, 0.0F, -10.0F, 0.0F));
		invTitle.setText(getPlayer().getInventory().getName());
		invTitle.setColor(4210752);
		
		mainTab.add(invTitle);
		
		playerSlots = Slots.addPlayerInventory(invPos, Size.of(18.0F, 18.0F, 0.0F), mainTab, getPlayer().getInventory());
		
		if (entity.hasEnergyStorage()) {
			energyBar = new EnergyBarWidget();
			energyBar.setPosition( Position.of(mainTab, 7.0F, 11.0F, 0.0F));
			energyBar.setSize( Size.of(24.0F, 48.0F, 0.0F));
			energyBar.setCurrent(() -> (float) entity.getEnergyStorage().getAmount());
			energyBar.setMaximum(() -> (float) entity.getEnergyStorage().getCapacity());
			
			mainTab.add(energyBar);
		}
		
		if (entity.hasFluidStorage()) {
			fluidBar = new FluidBarWidget();
			
			if (energyBar == null) {
				fluidBar.setPosition(Position.of(mainTab, 7.0F, 0.0F, 0.0F));
			} else {
				fluidBar.setPosition(Position.of(energyBar, 7.0F, 0.0F, 0.0F));
			}
			
			fluidBar.setSize(Size.of(24.0F, 48.0F, 0.0F));
			fluidBar.setStorage(entity.getFluidStorage().getStorage(0));
		}
	}
}
