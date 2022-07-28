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

package com.github.mixinors.astromine.common.screen.handler.base.entity;

import com.github.mixinors.astromine.common.entity.base.ExtendedEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.api.common.util.SlotUtil;
import dev.vini2003.hammer.gui.api.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import dev.vini2003.hammer.gui.api.common.widget.tab.TabWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextWidget;
import dev.vini2003.hammer.gui.energy.api.common.widget.bar.EnergyBarWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;

public abstract class ExtendedEntityScreenHandler extends BaseScreenHandler {
	public static final float SLOT_WIDTH = ExtendedBlockEntityScreenHandler.SLOT_WIDTH;
	public static final float SLOT_HEIGHT = ExtendedBlockEntityScreenHandler.SLOT_HEIGHT;
	
	public static final float BAR_HEIGHT = ExtendedBlockEntityScreenHandler.BAR_HEIGHT;
	public static final float BAR_WIDTH = ExtendedBlockEntityScreenHandler.BAR_WIDTH;
	
	public static final float REDSTONE_WIDTH = ExtendedBlockEntityScreenHandler.REDSTONE_WIDTH;
	public static final float REDSTONE_HEIGHT = ExtendedBlockEntityScreenHandler.REDSTONE_HEIGHT;
	
	public static final float TABS_WIDTH = ExtendedBlockEntityScreenHandler.TABS_WIDTH;
	public static final float TABS_HEIGHT = ExtendedBlockEntityScreenHandler.TABS_HEIGHT;
	
	public static final float ARROW_WIDTH = ExtendedBlockEntityScreenHandler.ARROW_WIDTH;
	public static final float ARROW_HEIGHT = ExtendedBlockEntityScreenHandler.ARROW_HEIGHT;
	
	public static final float FILTER_WIDTH = ExtendedBlockEntityScreenHandler.FILTER_WIDTH;
	public static final float FILTER_HEIGHT = ExtendedBlockEntityScreenHandler.FILTER_HEIGHT;
	
	public static final float LAUNCH_BUTTON_WIDTH = ExtendedBlockEntityScreenHandler.LAUNCH_BUTTON_WIDTH;
	public static final float LAUNCH_BUTTON_HEIGHT = ExtendedBlockEntityScreenHandler.LAUNCH_BUTTON_HEIGHT;
	
	public static final float PAD_2 = ExtendedBlockEntityScreenHandler.PAD_2;
	public static final float PAD_3 = ExtendedBlockEntityScreenHandler.PAD_3;
	public static final float PAD_4 = ExtendedBlockEntityScreenHandler.PAD_4;
	public static final float PAD_5 = ExtendedBlockEntityScreenHandler.PAD_5;
	public static final float PAD_7 = ExtendedBlockEntityScreenHandler.PAD_7;
	public static final float PAD_8 = ExtendedBlockEntityScreenHandler.PAD_8;
	public static final float PAD_10 = ExtendedBlockEntityScreenHandler.PAD_10;
	public static final float PAD_11 = ExtendedBlockEntityScreenHandler.PAD_11;
	public static final float PAD_25 = ExtendedBlockEntityScreenHandler.PAD_25;
	public static final float PAD_38 = ExtendedBlockEntityScreenHandler.PAD_38;
	public static final float PAD_68 = ExtendedBlockEntityScreenHandler.PAD_68;
	
	protected final ExtendedEntity entity;
	
	protected EnergyBarWidget energyBar = null;
	protected FluidBarWidget fluidBar = null;
	
	protected Collection<SlotWidget> playerSlots = new HashSet<>();
	
	protected TabWidget tabs;
	
	protected TabWidget.TabCollection tab;
	
	public ExtendedEntityScreenHandler(Supplier<? extends ScreenHandlerType<?>> type, int syncId, PlayerEntity player, int entityId) {
		super(type.get(), syncId, player);
		
		entity = (ExtendedEntity) player.world.getEntityById(entityId);
		
		if (!player.world.isClient) {
			entity.setSyncItemStorage(true);
			entity.setSyncFluidStorage(true);
		}
	}
	
	public abstract ItemStack getSymbol();
	
	public Size getTabsSizeExtension() {
		return new Size(0.0F, 0.0F);
	}
	
	public int getDefaultFluidSlotForBar() {
		return 0;
	}
	
	@Override
	public void init(int width, int height) {
		var symbol = getSymbol();
		
		var player = getPlayer();
		var inventory = player.getInventory();
		
		var tabsExtension = getTabsSizeExtension();
		
		tabs = new TabWidget();
		tabs.setSize(new Size(TABS_WIDTH + tabsExtension.getWidth(), TABS_HEIGHT + tabsExtension.getHeight(), 0.0F));
		tabs.setPosition(new Position(width / 2.0F - tabs.getWidth() / 2.0F, height / 2.0F - tabs.getHeight() / 2.0F, 0.0F));
		
		add(tabs);
		
		tab = tabs.addTab(() -> symbol, () -> ImmutableList.of(entity.getDisplayName()));
		tab.setPosition(new Position(tabs, 0.0F, PAD_25 + PAD_7));
		tab.setSize(new Size(TABS_WIDTH, TABS_HEIGHT));
		
		var title = new TextWidget();
		title.setPosition(new Position(tab, PAD_8, 0.0F));
		title.setText(entity.getDisplayName());
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
		
		if (entity.hasEnergyStorage()) {
			energyBar = new EnergyBarWidget();
			energyBar.setPosition(new Position(tab, PAD_7, PAD_11));
			energyBar.setSize(new Size(BAR_WIDTH, BAR_HEIGHT));
			energyBar.setStorage(() -> entity.getEnergyStorage());
			energyBar.setSmooth(false);
			energyBar.setCurrent(() -> (float) entity.getEnergyStorage().getAmount());
			energyBar.setMaximum(() -> (float) entity.getEnergyStorage().getCapacity());
			
			tab.add(energyBar);
		}
		
		if (entity.hasFluidStorage()) {
			fluidBar = new FluidBarWidget();
			
			if (energyBar == null) {
				fluidBar.setPosition(new Position(tab, PAD_7, PAD_11));
			} else {
				fluidBar.setPosition(new Position(energyBar, energyBar.getWidth() + PAD_7, 0.0F));
			}
			
			fluidBar.setSize(new Size(BAR_WIDTH, BAR_HEIGHT));
			fluidBar.setStorageView(() -> entity.getFluidStorage().getStorage(getDefaultFluidSlotForBar()));
			fluidBar.setSmooth(false);
			
			tab.add(fluidBar);
		}
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return this.entity.isAlive() && this.entity.distanceTo(player) < 8.0F;
	}
	
	public ExtendedEntity getEntity() {
		return entity;
	}
}
