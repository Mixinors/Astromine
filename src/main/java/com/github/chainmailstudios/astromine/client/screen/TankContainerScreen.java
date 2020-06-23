package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.BetaScreen;
import com.github.chainmailstudios.astromine.common.container.base.BetaContainer;
import com.github.chainmailstudios.astromine.common.container.FluidTankContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class TankContainerScreen extends BetaScreen<FluidTankContainer> {
	public TankContainerScreen(Text name, BetaContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
