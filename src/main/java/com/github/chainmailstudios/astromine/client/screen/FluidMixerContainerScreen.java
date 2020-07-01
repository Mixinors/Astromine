package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.common.block.entity.FluidMixerBlockEntity;
import com.github.chainmailstudios.astromine.common.widget.WHorizontalArrow;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyFluidContainerScreen;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.container.FluidMixerContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyFluidContainer;
import com.github.chainmailstudios.astromine.common.widget.WFluidVolumeFractionalVerticalBar;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class FluidMixerContainerScreen extends DefaultedEnergyFluidContainerScreen<FluidMixerContainer> {
	public FluidMixerContainerScreen(Text name, DefaultedEnergyFluidContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);

		FluidMixerBlockEntity mixer = (FluidMixerBlockEntity) linkedContainer.blockEntity;

		ComponentProvider componentProvider = linkedContainer.blockEntity;

		WFluidVolumeFractionalVerticalBar secondInputFluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(fluidBar, fluidBar.getWidth() + 4, 0, 0), Size.of(fluidBar));

		secondInputFluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(1));

		WHorizontalArrow arrow = mainPanel.createChild(WHorizontalArrow::new, Position.of(secondInputFluidBar, secondInputFluidBar.getWidth() + 9, secondInputFluidBar.getHeight() / 2 - 8, 0), Size.of(22, 16))
				.setLimitSupplier(() -> mixer.limit)
				.setProgressSupplier(() -> mixer.current);

		WFluidVolumeFractionalVerticalBar outputFluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(arrow, arrow.getWidth() + 7, -secondInputFluidBar.getHeight() / 2 + 8, 0), Size.of(fluidBar));

		outputFluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(2));
	}
}
