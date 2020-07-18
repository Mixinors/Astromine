package com.github.chainmailstudios.astromine.client.screen.base;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.client.screen.BaseHandledScreen;
import spinnery.common.handler.BaseScreenHandler;
import spinnery.widget.WPanel;

public abstract class DefaultedHandledScreen<T extends BaseScreenHandler> extends BaseHandledScreen<T> {
	public DefaultedHandledScreen(Text name, T linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
		this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);
		super.render(matrices, mouseX, mouseY, tickDelta);
	}

	protected void addTitle(WPanel panel) {
		panel.setLabel(this.getTitle());
	}
}
