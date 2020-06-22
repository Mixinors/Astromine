package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.common.container.EpsilonContainer;
import com.github.chainmailstudios.astromine.common.container.NuclearWarheadContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class NuclearWarheadContainerScreen extends EpsilionScreen<NuclearWarheadContainer> {
	public NuclearWarheadContainerScreen(Text name, EpsilonContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
