package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedBlockEntityContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedFluidContainer;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.common.widget.WFluidVolumeFractionalVerticalBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.common.container.BaseContainer;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public abstract class DefaultedFluidContainerScreen<T extends DefaultedBlockEntityContainer> extends DefaultedBlockEntityContainerScreen<T> {
	public WFluidVolumeFractionalVerticalBar fluidBar;

	public DefaultedFluidContainerScreen(Text name, DefaultedFluidContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);

		fluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(mainPanel, 7, 7, 0), Size.of(24, 48));

		ComponentProvider componentProvider = linkedContainer.blockEntity;

		fluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(0));
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
		this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);
		super.render(matrices, mouseX, mouseY, tickDelta);
	}
}
