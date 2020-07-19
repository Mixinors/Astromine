package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedItemHandledScreen;
import com.github.chainmailstudios.astromine.common.screenhandler.BufferScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.WSlot;
import spinnery.widget.WVerticalScrollableContainer;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Comparator;

public class BufferHandledScreen extends DefaultedItemHandledScreen<BufferScreenHandler> {
	public BufferHandledScreen(Text name, BufferScreenHandler handler, PlayerEntity player) {
		super(name, handler, player);

		int height = handler.bufferType.getHeight();

		int slotWidth = 9 * 18 + (9 * 2) + 7;
		int slotHeight = 6 * 18;

		int leftPadding = 6;
		int rightPadding = 6;

		int middlePadding = 18;

		int topPadding = 6 + 12;
		int bottomPadding = 6;

		int inventoryWidth = 9 * 18;
		int inventoryHeight = 3 * 18 + 7 + 18 + 9;

		int panelWidth = leftPadding + inventoryWidth + rightPadding + (9 * 2) + 8;
		int panelHeight = topPadding + slotHeight + middlePadding + inventoryHeight + bottomPadding;

		mainTabbedPanel.setSize(Size.of(panelWidth, panelHeight));
		mainTabbedPanel.onAlign();

		WVerticalScrollableContainer scrollableContainer = mainPanel.createChild(WVerticalScrollableContainer::new, Position.of(mainPanel, leftPadding, topPadding), Size.of(slotWidth, slotHeight));

		scrollableContainer.setDivisionSpace(1);

		for (int h = 0; h < height; ++h) {
			WSlot[] slots = WSlot.addHeadlessArray(scrollableContainer, 9 * h, 1, 9, 1).stream().peek(slot -> {
				slot.setSize(Size.of(18, 18));
			}).sorted(Comparator.comparing(WSlot::getSlotNumber)).toArray(WSlot[]::new);

			scrollableContainer.addRow(slots);
		}

		for (WSlot slot : playerSlots) {
			slot.setPosition(slot.getPosition().add(10.5f, 54, 0));
		}
	}
}
