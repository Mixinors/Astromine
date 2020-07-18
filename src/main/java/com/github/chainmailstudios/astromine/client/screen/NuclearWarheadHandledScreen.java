package com.github.chainmailstudios.astromine.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedHandledScreen;
import com.github.chainmailstudios.astromine.common.screenhandler.NuclearWarheadScreenHandler;

public class NuclearWarheadHandledScreen extends DefaultedHandledScreen<NuclearWarheadScreenHandler> {
	public NuclearWarheadHandledScreen(Text name, NuclearWarheadScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);
	}
}
