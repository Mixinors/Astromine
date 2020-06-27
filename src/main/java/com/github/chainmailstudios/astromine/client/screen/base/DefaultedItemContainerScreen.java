package com.github.chainmailstudios.astromine.client.screen.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.common.container.base.DefaultedBlockEntityContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedItemContainer;

public abstract class DefaultedItemContainerScreen<T extends DefaultedBlockEntityContainer> extends DefaultedBlockEntityContainerScreen<T> {
	public DefaultedItemContainerScreen(Text name, DefaultedItemContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);
	}
}
