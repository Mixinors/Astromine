package com.github.chainmailstudios.astromine.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedFluidContainerScreen;
import com.github.chainmailstudios.astromine.common.container.FluidTankContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedFluidContainer;

public class TankContainerScreen extends DefaultedFluidContainerScreen<FluidTankContainer> {
	public TankContainerScreen(Text name, DefaultedFluidContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
