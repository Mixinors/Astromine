package com.github.chainmailstudios.astromine.client.screen.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedBlockEntityScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.WEnergyVolumeFractionalVerticalBar;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public abstract class DefaultedEnergyHandledScreen<T extends DefaultedBlockEntityScreenHandler> extends DefaultedBlockEntityHandledScreen<T> {
	public WEnergyVolumeFractionalVerticalBar energyBar;

	public DefaultedEnergyHandledScreen(Text name, DefaultedEnergyScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, (T) linkedScreenHandler, player);

		energyBar = mainPanel.createChild(WEnergyVolumeFractionalVerticalBar::new, Position.of(mainPanel, 7, 20, 0), Size.of(24, 48));
		energyBar.setEnergyVolume(linkedScreenHandler.blockEntity::getEnergyVolume);
	}
}
