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

package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedFacingBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.base.DefaultedHorizontalFacingBlockWithEntity;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.NameableComponent;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedBlockEntityScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.WTabbedPanel;
import com.github.chainmailstudios.astromine.common.widget.WTransferTypeSelectorPanel;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WSlot;
import spinnery.widget.WTabHolder;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Collection;

public class DefaultedBlockEntityHandledScreen<T extends DefaultedBlockEntityScreenHandler> extends DefaultedHandledScreen<T> {
	public WInterface mainInterface;
	public WTabbedPanel mainTabbedPanel;
	public WPanel mainPanel;
	public Collection<WSlot> playerSlots;

	public DefaultedBlockEntityHandledScreen(Text name, T handler, PlayerEntity player) {
		super(name, handler, player);

		mainInterface = getInterface();

		mainTabbedPanel = mainInterface.createChild(WTabbedPanel::new, Position.ORIGIN, Size.of(176, 160 + 24));
		WTabHolder.WTab mainTab = mainTabbedPanel.addTab(handler.getWorld().getBlockState(handler.syncBlockEntity.getPos()).getBlock().asItem());
		mainPanel = mainTab.getBody();

		MinecraftClient.getInstance().mouse.unlockCursor();
		addTitle(mainPanel);

		mainTab.setInterface(getInterface());
		mainPanel.setInterface(getInterface());

		mainTabbedPanel.center();
		mainTabbedPanel.setOnAlign(widget -> {
			widget.center();
			widget.setY(widget.getY() - 12);
		});

		playerSlots = Sets.newHashSet(WSlot.addPlayerInventory(Position.of(mainPanel, 7, mainPanel.getHeight() - 18 - 11 - (18 * 3), 2), Size.of(18, 18), mainPanel));

		ComponentProvider componentProvider = ComponentProvider.fromBlockEntity(handler.syncBlockEntity);

		Direction rotation = Direction.NORTH;
		Block block = handler.syncBlockEntity.getCachedState().getBlock();

		if (block instanceof DefaultedHorizontalFacingBlockWithEntity) {
			rotation = handler.syncBlockEntity.getCachedState().get(HorizontalFacingBlock.FACING);
		} else if (block instanceof DefaultedFacingBlockWithEntity) {
			rotation = handler.syncBlockEntity.getCachedState().get(FacingBlock.FACING);
		}

		final Direction finalRotation = rotation;

		BlockEntityTransferComponent transferComponent = componentProvider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

		transferComponent.get().forEach((type, entry) -> {
			if (type != null) {
				if (componentProvider.getComponent(type) instanceof NameableComponent) {
					NameableComponent nameableComponent = (NameableComponent) componentProvider.getComponent(type);
					WTabHolder.WTab tab = mainTabbedPanel.addTab(nameableComponent.getSymbol());
					WTransferTypeSelectorPanel.createTab(tab, Position.of(mainTabbedPanel, mainTabbedPanel.getWidth() / 2 - 38, 0, 0), finalRotation, transferComponent, handler.syncBlockEntity.getPos(), type, getInterface());
					tab.getBody().setLabel(nameableComponent.getName());
					playerSlots.addAll(WSlot.addPlayerInventory(Position.of(mainTabbedPanel, 7, mainTabbedPanel.getHeight() - 18 - 11 - (18 * 3), 2), Size.of(18, 18), tab.getBody()));
				}
			}
		});
	}
}
