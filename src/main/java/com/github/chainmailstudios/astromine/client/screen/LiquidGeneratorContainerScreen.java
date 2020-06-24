package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyFluidContainerScreen;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyFluidContainer;
import com.github.chainmailstudios.astromine.common.container.LiquidGeneratorContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class LiquidGeneratorContainerScreen extends DefaultedEnergyFluidContainerScreen<LiquidGeneratorContainer> {
	public LiquidGeneratorContainerScreen(Text name, DefaultedEnergyFluidContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
