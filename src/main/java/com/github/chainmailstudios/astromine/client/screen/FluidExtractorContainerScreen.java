package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyFluidContainerScreen;
import com.github.chainmailstudios.astromine.common.container.FluidExtractorContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyFluidContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class FluidExtractorContainerScreen extends DefaultedEnergyFluidContainerScreen<FluidExtractorContainer> {
	public FluidExtractorContainerScreen(Text name, DefaultedEnergyFluidContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
