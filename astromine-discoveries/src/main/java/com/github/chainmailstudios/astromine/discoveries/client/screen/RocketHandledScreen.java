package com.github.chainmailstudios.astromine.discoveries.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.entity.ComponentEntityFluidHandledScreen;
import com.github.chainmailstudios.astromine.discoveries.common.screenhandler.RocketScreenHandler;
import com.github.vini2003.blade.common.handler.BaseScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class RocketHandledScreen extends ComponentEntityFluidHandledScreen<RocketScreenHandler> {
	public RocketHandledScreen(BaseScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}
}
