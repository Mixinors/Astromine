package com.github.chainmailstudios.astromine.client.screen.base;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.common.container.base.DefaultedBlockStateContainer;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.common.container.BaseContainer;

public abstract class DefaultedContainerScreen<T extends BaseContainer> extends BaseContainerScreen<T> {
	public DefaultedContainerScreen(Text name, DefaultedBlockStateContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
		this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);
		super.render(matrices, mouseX, mouseY, tickDelta);
	}
}
