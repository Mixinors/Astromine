package com.github.chainmailstudios.astromine.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyItemHandledScreen;
import com.github.chainmailstudios.astromine.common.screenhandler.BlockPlacerScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyItemScreenHandler;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class BlockPlacerHandledScreen extends DefaultedEnergyItemHandledScreen<BlockPlacerScreenHandler> {
	public BlockPlacerHandledScreen(Text name, DefaultedEnergyItemScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);

		mainPanel.createChild(WSlot::new, Position.of(mainPanel, mainPanel.getWidth() / 2 - 9, 35, 0), Size.of(18, 18)).setInventoryNumber(1).setSlotNumber(0);
	}
}
