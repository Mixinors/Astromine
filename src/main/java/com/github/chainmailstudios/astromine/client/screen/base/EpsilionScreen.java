package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.container.base.EpsilonContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.common.container.BaseContainer;

public abstract class EpsilionScreen<T extends BaseContainer> extends BaseContainerScreen<T> {
	public EpsilionScreen(Text name, EpsilonContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);
	}
}
