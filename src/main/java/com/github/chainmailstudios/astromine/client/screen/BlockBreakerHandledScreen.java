package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyItemHandledScreen;
import com.github.chainmailstudios.astromine.common.screenhandler.BlockBreakerScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyItemScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class BlockBreakerHandledScreen extends DefaultedEnergyItemHandledScreen<BlockBreakerScreenHandler> {
	public BlockBreakerHandledScreen(Text name, DefaultedEnergyItemScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);

		mainPanel.createChild(WSlot::new, Position.of(mainPanel, mainPanel.getWidth() / 2 - 9, 35, 0), Size.of(18, 18)).setInventoryNumber(1).setSlotNumber(0);
	}
}
