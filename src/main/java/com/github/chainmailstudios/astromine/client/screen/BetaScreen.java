package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.container.BetaContainer;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.common.widget.WFluidVolumeFractionalVerticalBar;
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

public abstract class BetaScreen<T extends BaseContainer> extends BaseContainerScreen<T> {
	public BetaScreen(Text name, BetaContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);

		WInterface mainInterface = getInterface();

		WPanel mainPanel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(176, 160));

		mainPanel.center();
		mainPanel.setOnAlign(WAbstractWidget::center);

		WSlot.addPlayerInventory(Position.of(mainPanel, 7, 77, 0), Size.of(18, 18), mainPanel);

		WFluidVolumeFractionalVerticalBar fluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(mainPanel, 7, 7, 0), Size.of(24, 48));

		ComponentProvider componentProvider = linkedContainer.blockEntity;

		FluidVolume fluidVolume = componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(0);

		fluidBar.setFluidVolume(fluidVolume);
	}
}
