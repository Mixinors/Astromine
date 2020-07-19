package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedFluidHandledScreen;
import com.github.chainmailstudios.astromine.common.screenhandler.FluidTankScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedFluidScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class TankHandledScreen extends DefaultedFluidHandledScreen<FluidTankScreenHandler> {
	public TankHandledScreen(Text name, DefaultedFluidScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);
	}
}
