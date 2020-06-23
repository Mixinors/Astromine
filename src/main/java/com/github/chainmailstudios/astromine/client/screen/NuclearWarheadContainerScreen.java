package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedContainerScreen;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedContainer;
import com.github.chainmailstudios.astromine.common.container.NuclearWarheadContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class NuclearWarheadContainerScreen extends DefaultedContainerScreen<NuclearWarheadContainer> {
	public NuclearWarheadContainerScreen(Text name, DefaultedContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
