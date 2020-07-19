package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyHandledScreen;
import com.github.chainmailstudios.astromine.common.screenhandler.CreativeCapacitorScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class CreativeCapacitorHandledScreen extends DefaultedEnergyHandledScreen<CreativeCapacitorScreenHandler> {
	public CreativeCapacitorHandledScreen(Text name, DefaultedEnergyScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);
	}
}
