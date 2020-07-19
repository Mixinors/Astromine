package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyFluidHandledScreen;
import com.github.chainmailstudios.astromine.common.screenhandler.FluidInserterScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyFluidScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class FluidInserterHandledScreen extends DefaultedEnergyFluidHandledScreen<FluidInserterScreenHandler> {
	public FluidInserterHandledScreen(Text name, DefaultedEnergyFluidScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);
	}
}
