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

package com.github.chainmailstudios.astromine.common.screenhandler.base;

import com.github.chainmailstudios.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.AbstractBlockEntity;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.NameableComponent;
import com.github.chainmailstudios.astromine.common.widget.TransferTypeSelectorPanelUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.vini2003.blade.common.data.Position;
import com.github.vini2003.blade.common.data.Size;
import com.github.vini2003.blade.common.data.Slots;
import com.github.vini2003.blade.common.data.widget.TabCollection;
import com.github.vini2003.blade.common.handler.BaseScreenHandler;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import com.github.vini2003.blade.common.widget.base.TabWidget;
import com.github.vini2003.blade.common.widget.base.TextWidget;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public abstract class DefaultedBlockEntityScreenHandler extends BaseScreenHandler {
	public AbstractBlockEntity syncBlockEntity;
	public Collection<SlotWidget> playerSlots = new HashSet<>();
	public TabCollection mainTab;
	protected TabWidget tabs;

	public DefaultedBlockEntityScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerEntity player, BlockPos position) {
		super(type, syncId, player);

		syncBlockEntity = (AbstractBlockEntity) player.world.getBlockEntity(position);

		if (!player.world.isClient) {
			syncBlockEntity.doNotSkipInventory();
			syncBlockEntity.sync();
		}
	}

	public int getTabWidgetExtendedHeight() {
		return 0;
	}

	@Override
	public void initialize(int width, int height) {
		tabs = new TabWidget();
		tabs.setSize(Size.of(176F, 188F + getTabWidgetExtendedHeight()));
		tabs.setPosition(Position.of(width / 2 - tabs.getWidth() / 2, height / 2 - tabs.getHeight() / 2));

		addWidget(tabs);

		mainTab = (TabCollection) tabs.addTab(syncBlockEntity.getCachedState().getBlock().asItem());
		mainTab.setPosition(Position.of(tabs, 0, 25F + 7F));
		mainTab.setSize(Size.of(176F, 184F));

		TextWidget title = new TextWidget();
		title.setPosition(Position.of(mainTab, 8, 0));
		title.setText(new TranslatableText(syncBlockEntity.getCachedState().getBlock().asItem().getTranslationKey()));
		title.setColor(4210752);
		mainTab.addWidget(title);

		Position invPos = Position.of(tabs, 7F, 25F + 7F + (184 - 18 - 18 - (18 * 4) - 3 + getTabWidgetExtendedHeight()));
		TextWidget invTitle = new TextWidget();
		invTitle.setPosition(Position.of(invPos, 0, -10));
		invTitle.setText(getPlayer().inventory.getName());
		invTitle.setColor(4210752);
		mainTab.addWidget(invTitle);
		playerSlots = Slots.addPlayerInventory(invPos, Size.of(18F, 18F), mainTab, getPlayer().inventory);

		ComponentProvider componentProvider = ComponentProvider.fromBlockEntity(syncBlockEntity);

		Direction rotation = Direction.NORTH;
		Block block = syncBlockEntity.getCachedState().getBlock();

		if (block instanceof HorizontalFacingBlockWithEntity) {
			DirectionProperty property = ((HorizontalFacingBlockWithEntity) block).getDirectionProperty();
			if (property != null)
				rotation = syncBlockEntity.getCachedState().get(property);
		}

		final Direction finalRotation = rotation;

		BlockEntityTransferComponent transferComponent = componentProvider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

		transferComponent.get().forEach((type, entry) -> {
			if (componentProvider.getComponent(type) instanceof NameableComponent) {
				NameableComponent nameableComponent = (NameableComponent) componentProvider.getComponent(type);
				TabCollection current = (TabCollection) tabs.addTab(nameableComponent.getSymbol(), () -> Collections.singletonList(nameableComponent.getName()));
				TransferTypeSelectorPanelUtilities.createTab(current, Position.of(tabs, tabs.getWidth() / 2 - 38, getTabWidgetExtendedHeight() / 2), finalRotation, transferComponent, syncBlockEntity.getPos(), type);
				TextWidget invTabTitle = new TextWidget();
				invTabTitle.setPosition(Position.of(invPos, 0, -10));
				invTabTitle.setText(getPlayer().inventory.getName());
				invTabTitle.setColor(4210752);
				current.addWidget(invTabTitle);
				playerSlots.addAll(Slots.addPlayerInventory(invPos, Size.of(18F, 18F), current, getPlayer().inventory));

				TextWidget tabTitle = new TextWidget();
				tabTitle.setPosition(Position.of(mainTab, 8, 0));
				tabTitle.setText(nameableComponent.getName());
				tabTitle.setColor(4210752);
				current.addWidget(tabTitle);
			}
		});
	}
}
