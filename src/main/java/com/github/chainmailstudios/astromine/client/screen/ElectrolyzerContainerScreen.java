package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyFluidContainerScreen;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.container.ElectrolyzerContainer;
import com.github.chainmailstudios.astromine.common.container.FluidExtractorContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyFluidContainer;
import com.github.chainmailstudios.astromine.common.widget.WFluidVolumeFractionalVerticalBar;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class ElectrolyzerContainerScreen extends DefaultedEnergyFluidContainerScreen<ElectrolyzerContainer> {
	public ElectrolyzerContainerScreen(Text name, DefaultedEnergyFluidContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);

		WFluidVolumeFractionalVerticalBar outputFluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(fluidBar, fluidBar.getWidth() + 4, 0, 0), Size.of(fluidBar));

		ComponentProvider componentProvider = linkedContainer.blockEntity;

		outputFluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(1));
	}
}
