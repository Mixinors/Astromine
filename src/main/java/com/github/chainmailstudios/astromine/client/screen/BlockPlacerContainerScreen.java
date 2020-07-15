package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyItemContainerScreen;
import com.github.chainmailstudios.astromine.common.container.BlockBreakerContainer;
import com.github.chainmailstudios.astromine.common.container.BlockPlacerContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyItemContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class BlockPlacerContainerScreen extends DefaultedEnergyItemContainerScreen<BlockPlacerContainer> {
	public BlockPlacerContainerScreen(Text name, DefaultedEnergyItemContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);

		mainPanel.createChild(WSlot::new, Position.of(mainPanel, mainPanel.getWidth() / 2 - 9, 35, 0), Size.of(18, 18)).setInventoryNumber(1).setSlotNumber(0);
	}
}
