package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.FoxtrotScreen;
import com.github.chainmailstudios.astromine.common.container.CreativeBufferContainer;
import com.github.chainmailstudios.astromine.common.container.base.FoxtrotContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class CreativeBufferContainerScreen extends FoxtrotScreen<CreativeBufferContainer> {
	public CreativeBufferContainerScreen(Text name, FoxtrotContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);

		mainPanel.createChild(WSlot::new, Position.of(mainPanel, 0, 22, 0), Size.of(36, 36)).setSlotNumber(0).setInventoryNumber(1).centerX();
	}
}
