package com.github.chainmailstudios.astromine.client.screen.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedBlockEntityContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyContainer;
import com.github.chainmailstudios.astromine.common.widget.WEnergyVolumeFractionalVerticalBar;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public abstract class DefaultedEnergyContainerScreen<T extends DefaultedBlockEntityContainer> extends DefaultedBlockEntityContainerScreen<T> {
	public WEnergyVolumeFractionalVerticalBar energyBar;

	public DefaultedEnergyContainerScreen(Text name, DefaultedEnergyContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);

		energyBar = mainPanel.createChild(WEnergyVolumeFractionalVerticalBar::new, Position.of(mainPanel, 7, 20, 0), Size.of(24, 48));

		ComponentProvider componentProvider = linkedContainer.blockEntity;

		energyBar.setEnergyVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT).getVolume(0));
	}
}
