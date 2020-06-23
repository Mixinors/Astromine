package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.AlphaScreen;
import com.github.chainmailstudios.astromine.common.container.base.AlphaContainer;
import com.github.chainmailstudios.astromine.common.container.LiquidGeneratorContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class LiquidGeneratorContainerScreen extends AlphaScreen<LiquidGeneratorContainer> {
	public LiquidGeneratorContainerScreen(Text name, AlphaContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);
	}
}
