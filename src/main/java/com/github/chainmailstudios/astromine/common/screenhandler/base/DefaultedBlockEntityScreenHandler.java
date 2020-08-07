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
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedBlockEntity;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.NameableComponent;
import com.github.chainmailstudios.astromine.common.widget.TransferTypeSelectorPanelUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.vini2003.blade.common.data.Position;
import com.github.vini2003.blade.common.data.Size;
import com.github.vini2003.blade.common.data.Slots;
import com.github.vini2003.blade.common.handler.BaseScreenHandler;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import com.github.vini2003.blade.common.widget.base.TabWidget;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Collection;
import java.util.HashSet;

public abstract class DefaultedBlockEntityScreenHandler extends BaseScreenHandler {
	public DefaultedBlockEntity syncBlockEntity;

	public Collection<SlotWidget> playerSlots = new HashSet<>();

	public com.github.vini2003.blade.common.data.widget.Collection mainTab;

	public DefaultedBlockEntityScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerEntity player, BlockPos position) {
		super(type, syncId, player);

		syncBlockEntity = (DefaultedBlockEntity) player.world.getBlockEntity(position);

		if (!player.world.isClient) {
			syncBlockEntity.doNotSkipInventory();
			syncBlockEntity.sync();
		}
	}

	@Override
	public void initialize(int width, int height) {
		TabWidget tabs = new TabWidget();
		tabs.setPosition(new Position(() -> (float) (width / 2 - 176 / 2), () -> (float) (height / 2 - 184 / 2)));
		tabs.setSize(new Size(() -> 176F, () -> 184F));

		addWidget(tabs);

		mainTab = (com.github.vini2003.blade.common.data.widget.Collection) tabs.addTab(syncBlockEntity.getCachedState().getBlock().asItem());

		playerSlots = Slots.addPlayerInventory(new Position(() -> tabs.getPosition().getX() + 7F, () -> tabs.getPosition().getY() + 25F + 7F + (184 / 2 - 18 - 11 - (18 * 3))), new Size(() -> 18F, () -> 18F), tabs, getPlayer().inventory);

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
				com.github.vini2003.blade.common.data.widget.Collection current = (com.github.vini2003.blade.common.data.widget.Collection) tabs.addTab(nameableComponent.getSymbol(), () -> Lists.newArrayList(nameableComponent.getName()));
				TransferTypeSelectorPanelUtilities.createTab(current, new Position(tabs.getPosition().getX() + tabs.getSize().getWidth() / 2 - 38, 0), finalRotation, transferComponent, syncBlockEntity.getPos(), type);
				playerSlots.addAll(Slots.addPlayerInventory(new Position(() -> tabs.getPosition().getX() + 7F, () -> tabs.getPosition().getY() + 25F + 7F + (184 / 2 - 18 - 11 - (18 * 3))), new Size(() -> 18F, () -> 18F), current, getPlayer().inventory));
			}
		});
	}
}
