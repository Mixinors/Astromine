/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.common.screenhandler.base.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;

import com.github.chainmailstudios.astromine.common.entity.base.ComponentEntity;
import com.github.vini2003.blade.common.collection.TabWidgetCollection;
import com.github.vini2003.blade.common.handler.BaseScreenHandler;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.utilities.Slots;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import com.github.vini2003.blade.common.widget.base.TabWidget;
import com.github.vini2003.blade.common.widget.base.TextWidget;

import java.util.Collection;
import java.util.HashSet;

public abstract class ComponentEntityScreenHandler extends BaseScreenHandler {
	public ComponentEntity syncEntity;
	public Collection<SlotWidget> playerSlots = new HashSet<>();
	public TabWidgetCollection mainTab;
	protected TabWidget tabs;

	public ComponentEntityScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerEntity player, int entityId) {
		super(type, syncId, player);

		syncEntity = (ComponentEntity) player.world.getEntityById(entityId);
	}

	public abstract ItemStack getSymbol();

	public int getTabWidgetExtendedHeight() {
		return 0;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.syncEntity.isAlive() && this.syncEntity.distanceTo(player) < 8.0F;
	}

	@Override
	public void initialize(int width, int height) {
		tabs = new TabWidget();
		tabs.setSize(Size.of(176F, 188F + getTabWidgetExtendedHeight()));
		tabs.setPosition(Position.of(width / 2 - tabs.getWidth() / 2, height / 2 - tabs.getHeight() / 2));

		addWidget(tabs);

		mainTab = (TabWidgetCollection) tabs.addTab(getSymbol());
		mainTab.setPosition(Position.of(tabs, 0, 25F + 7F));
		mainTab.setSize(Size.of(176F, 184F));

		TextWidget title = new TextWidget();
		title.setPosition(Position.of(mainTab, 8, 0));
		title.setText(syncEntity.getDisplayName());
		title.setColor(4210752);
		mainTab.addWidget(title);

		Position invPos = Position.of(tabs, 7F, 25F + 7F + (184 - 18 - 18 - (18 * 4) - 3 + getTabWidgetExtendedHeight()));
		TextWidget invTitle = new TextWidget();
		invTitle.setPosition(Position.of(invPos, 0, -10));
		invTitle.setText(getPlayer().inventory.getName());
		invTitle.setColor(4210752);
		mainTab.addWidget(invTitle);
		playerSlots = Slots.addPlayerInventory(invPos, Size.of(18F, 18F), mainTab, getPlayer().inventory);
	}
}
