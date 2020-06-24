package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.container.base.DefaultedBlockStateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.common.container.BaseContainer;

public abstract class DefaultedContainerScreen<T extends BaseContainer> extends BaseContainerScreen<T> {
	public DefaultedContainerScreen(Text name, DefaultedBlockStateContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);
	}
}
