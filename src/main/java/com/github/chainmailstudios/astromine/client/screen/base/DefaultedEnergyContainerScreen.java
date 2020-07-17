package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.container.base.DefaultedBlockEntityContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyContainer;
import com.github.chainmailstudios.astromine.common.widget.WEnergyVolumeFractionalVerticalBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public abstract class DefaultedEnergyContainerScreen<T extends DefaultedBlockEntityContainer> extends DefaultedBlockEntityContainerScreen<T> {
	public WEnergyVolumeFractionalVerticalBar energyBar;

	public DefaultedEnergyContainerScreen(Text name, DefaultedEnergyContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);

		energyBar = mainPanel.createChild(WEnergyVolumeFractionalVerticalBar::new, Position.of(mainPanel, 7, 20, 0), Size.of(24, 48));
		energyBar.setEnergyVolume(linkedContainer.blockEntity::getEnergyVolume);
	}
}
