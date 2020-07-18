package com.github.chainmailstudios.astromine.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyFluidHandledScreen;
import com.github.chainmailstudios.astromine.common.screenhandler.FluidExtractorScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyFluidScreenHandler;

public class FluidExtractorHandledScreen extends DefaultedEnergyFluidHandledScreen<FluidExtractorScreenHandler> {
	public FluidExtractorHandledScreen(Text name, DefaultedEnergyFluidScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);
	}
}
