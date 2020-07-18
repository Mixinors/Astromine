package com.github.chainmailstudios.astromine.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedItemHandledScreen;
import com.github.chainmailstudios.astromine.common.screenhandler.CreativeBufferScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedItemScreenHandler;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class CreativeBufferHandledScreen extends DefaultedItemHandledScreen<CreativeBufferScreenHandler> {
	public CreativeBufferHandledScreen(Text name, DefaultedItemScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);

		mainPanel.createChild(WSlot::new, Position.of(mainPanel, 0, 22, 0), Size.of(36, 36)).setSlotNumber(0).setInventoryNumber(1).centerX();
	}
}
