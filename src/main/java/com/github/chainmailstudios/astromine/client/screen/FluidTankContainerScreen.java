package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.common.container.BetaContainer;
import com.github.chainmailstudios.astromine.common.container.FluidTankContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class FluidTankContainerScreen extends BetaScreen<FluidTankContainer> {
	public FluidTankContainerScreen(Text name, BetaContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
