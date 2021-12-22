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

import dev.vini2003.hammer.common.geometry.position.Position;
import dev.vini2003.hammer.common.geometry.size.Size;
import dev.vini2003.hammer.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.common.util.Slots;
import dev.vini2003.hammer.common.widget.slot.SlotWidget;
import dev.vini2003.hammer.common.widget.tab.TabWidget;
import dev.vini2003.hammer.common.widget.text.TextWidget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.entity.base.ExtendedEntity;
import com.github.mixinors.astromine.common.screenhandler.base.block.BlockStateScreenHandler;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;

/**
 * A {@link BlockStateScreenHandler} with an attached
 * {@link ExtendedBlockEntity}.
 */
public abstract class ComponentEntityScreenHandler extends BaseScreenHandler
{
	protected ExtendedEntity entity;

	protected Collection<SlotWidget> playerSlots = new HashSet<>();

	protected TabWidget tabs;

	protected TabWidget.TabWidgetCollection mainTab;

	/** Instantiates a {@link ComponentEntityScreenHandler},
	 * synchronizing its attached {@link ExtendedEntity}. */
	public ComponentEntityScreenHandler(Supplier<? extends ScreenHandlerType<?>> type, int syncId, PlayerEntity player, int entityId) {
		super(type.get(), syncId, player);

		entity = (ExtendedEntity) player.world.getEntityById(entityId);
	}

	/** Returns an {@link ItemStack} representing this entity in the {@link TabWidget}. */
	public abstract ItemStack getSymbol();

	/** Returns the additional height that the {@link TabWidget} should have.
	 * At that, I don't know why this method is a thing. */
	public int getTabWidgetExtendedHeight() {
		return 0;
	}

	/** Override behavior to only allow the {@link ScreenHandler} to be open
	 * when possible, and while the associated {@link Entity} has not died
	 * or moved too far away. */
	@Override
	public boolean canUse(PlayerEntity player) {
		return this.entity.isAlive() && this.entity.distanceTo(player) < 8.0F;
	}

	/** Override behavior to build the entity interface,
	 * instantiating and configuring the {@link TabWidget},
	 * its tabs, the inventory, and other miscellaneous things. */
	@Override
	public void initialize(int width, int height) {
		tabs = new TabWidget();
		tabs.setSize( Size.of(176F, 188F + getTabWidgetExtendedHeight()));
		tabs.setPosition( Position.of(width / 2 - tabs.getWidth() / 2, height / 2 - tabs.getHeight() / 2));

		add(tabs);

		mainTab = (TabWidget.TabWidgetCollection) tabs.addTab(getSymbol());
		mainTab.setPosition(Position.of(tabs, 0, 25F + 7F));
		mainTab.setSize(Size.of(176F, 184F));

		TextWidget title = new TextWidget();
		title.setPosition(Position.of(mainTab, 8, 0));
		title.setText(entity.getDisplayName());
		title.setColor(4210752);
		mainTab.add(title);

		Position invPos = Position.of(tabs, 7F, 25F + 7F + (184 - 18 - 18 - (18 * 4) - 3 + getTabWidgetExtendedHeight()));
		TextWidget invTitle = new TextWidget();
		invTitle.setPosition(Position.of(invPos, 0, -10));
		invTitle.setText(getPlayer().getInventory().getName());
		invTitle.setColor(4210752);
		mainTab.add(invTitle);
		playerSlots = Slots.addPlayerInventory(invPos, Size.of(18F, 18F), mainTab, getPlayer().getInventory());
	}
}
