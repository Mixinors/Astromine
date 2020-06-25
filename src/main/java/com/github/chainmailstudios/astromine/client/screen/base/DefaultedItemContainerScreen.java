package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.container.base.DefaultedBlockEntityContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedItemContainer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.common.container.BaseContainer;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Collection;

public abstract class DefaultedItemContainerScreen<T extends DefaultedBlockEntityContainer> extends DefaultedBlockEntityContainerScreen<T> {
	public DefaultedItemContainerScreen(Text name, DefaultedItemContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
		this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);
		super.render(matrices, mouseX, mouseY, tickDelta);
	}
}
