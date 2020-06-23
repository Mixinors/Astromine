package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyFluidContainerScreen;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyFluidContainer;
import com.github.chainmailstudios.astromine.common.container.VentContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class VentContainerScreen extends DefaultedEnergyFluidContainerScreen<VentContainer> {
	public VentContainerScreen(Text name, DefaultedEnergyFluidContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
