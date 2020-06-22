package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.container.AlphaContainer;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.widget.WEnergyVolumeFractionalVerticalBar;
import com.github.chainmailstudios.astromine.widget.WFluidVolumeFractionalVerticalBar;
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

public abstract class AlphaScreen<T extends BaseContainer> extends BaseContainerScreen<T> {
	public AlphaScreen(Text name, AlphaContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);

		WInterface mainInterface = getInterface();

		WPanel mainPanel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(176, 160));

		mainPanel.center();
		mainPanel.setOnAlign(WAbstractWidget::center);

		WSlot.addPlayerInventory(Position.of(mainPanel, 7, 77, 0), Size.of(18, 18), mainPanel);

		WEnergyVolumeFractionalVerticalBar energyBar = mainPanel.createChild(WEnergyVolumeFractionalVerticalBar::new, Position.of(mainPanel, 7, 7, 0), Size.of(24, 48));

		WFluidVolumeFractionalVerticalBar fluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(energyBar, energyBar.getWidth() + 4, 0, 0), Size.of(energyBar));

		ComponentProvider componentProvider = linkedContainer.blockEntity;

		EnergyVolume energyVolume = componentProvider.getComponent(null, EnergyInventoryComponent.class).getVolume(0);

		FluidVolume fluidVolume = componentProvider.getComponent(null, FluidInventoryComponent.class).getVolume(0);

		energyBar.setEnergyVolume(energyVolume);

		fluidBar.setFluidVolume(fluidVolume);
	}
}
