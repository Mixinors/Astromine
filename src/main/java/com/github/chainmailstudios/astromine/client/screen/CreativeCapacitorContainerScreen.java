package com.github.chainmailstudios.astromine.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyContainerScreen;
import com.github.chainmailstudios.astromine.common.container.CreativeCapacitorContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyContainer;

public class CreativeCapacitorContainerScreen extends DefaultedEnergyContainerScreen<CreativeCapacitorContainer> {
	public CreativeCapacitorContainerScreen(Text name, DefaultedEnergyContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
