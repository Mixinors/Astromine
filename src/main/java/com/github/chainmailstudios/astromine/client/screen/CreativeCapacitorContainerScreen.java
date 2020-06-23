package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DeltaScreen;
import com.github.chainmailstudios.astromine.common.container.CreativeCapacitorContainer;
import com.github.chainmailstudios.astromine.common.container.base.DeltaContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class CreativeCapacitorContainerScreen extends DeltaScreen<CreativeCapacitorContainer> {
	public CreativeCapacitorContainerScreen(Text name, DeltaContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
