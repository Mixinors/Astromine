package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.common.container.AlphaContainer;
import com.github.chainmailstudios.astromine.common.container.VentContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class VentContainerScreen extends AlphaScreen<VentContainer> {
	public VentContainerScreen(Text name, AlphaContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
