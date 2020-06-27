package com.github.chainmailstudios.astromine.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedContainerScreen;
import com.github.chainmailstudios.astromine.common.container.NuclearWarheadContainer;

public class NuclearWarheadContainerScreen extends DefaultedContainerScreen<NuclearWarheadContainer> {
	public NuclearWarheadContainerScreen(Text name, NuclearWarheadContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
