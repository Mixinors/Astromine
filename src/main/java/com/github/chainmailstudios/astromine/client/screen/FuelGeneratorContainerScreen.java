package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.common.container.AlphaContainer;
import com.github.chainmailstudios.astromine.common.container.FuelGeneratorContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class FuelGeneratorContainerScreen extends AlphaScreen<FuelGeneratorContainer> {
	public FuelGeneratorContainerScreen(Text name, AlphaContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
