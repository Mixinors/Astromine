package com.github.chainmailstudios.astromine.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyFluidContainerScreen;
import com.github.chainmailstudios.astromine.common.block.entity.ElectrolyzerBlockEntity;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.container.ElectrolyzerContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyFluidContainer;
import com.github.chainmailstudios.astromine.common.widget.WFluidVolumeFractionalVerticalBar;
import com.github.chainmailstudios.astromine.common.widget.WHorizontalArrow;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class ElectrolyzerContainerScreen extends DefaultedEnergyFluidContainerScreen<ElectrolyzerContainer> {
	public ElectrolyzerContainerScreen(Text name, DefaultedEnergyFluidContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);

		ElectrolyzerBlockEntity electrolyzer = (ElectrolyzerBlockEntity) linkedContainer.blockEntity;

		ComponentProvider componentProvider = linkedContainer.blockEntity;

		WFluidVolumeFractionalVerticalBar firstOutputFluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(fluidBar, fluidBar.getWidth() + 4 + 18 + 18, 0, 0), Size.of(fluidBar));

		firstOutputFluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(1));

		WFluidVolumeFractionalVerticalBar secondOutputFluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(firstOutputFluidBar, firstOutputFluidBar.getWidth() + 7, 0, 0), Size.of(fluidBar));

		secondOutputFluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(2));
		
		mainPanel.createChild(WHorizontalArrow::new, Position.of(fluidBar, fluidBar.getWidth() + 9, fluidBar.getHeight() / 2 - 8, 0), Size.of(22, 16))
				.setLimitSupplier(() -> electrolyzer.limit)
				.setProgressSupplier(() -> electrolyzer.current);
	}
}
