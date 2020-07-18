package com.github.chainmailstudios.astromine.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyFluidHandledScreen;
import com.github.chainmailstudios.astromine.common.block.entity.ElectrolyzerBlockEntity;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.screenhandler.ElectrolyzerScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyFluidScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.WFluidVolumeFractionalVerticalBar;
import com.github.chainmailstudios.astromine.common.widget.WHorizontalArrow;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class ElectrolyzerHandledScreen extends DefaultedEnergyFluidHandledScreen<ElectrolyzerScreenHandler> {
	public ElectrolyzerHandledScreen(Text name, DefaultedEnergyFluidScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);

		ElectrolyzerBlockEntity electrolyzer = (ElectrolyzerBlockEntity) linkedScreenHandler.blockEntity;

		ComponentProvider componentProvider = linkedScreenHandler.blockEntity;

		WFluidVolumeFractionalVerticalBar firstOutputFluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(fluidBar, fluidBar.getWidth() + 4 + 18 + 18, 0, 0), Size.of(fluidBar));

		firstOutputFluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(1));

		WFluidVolumeFractionalVerticalBar secondOutputFluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(firstOutputFluidBar, firstOutputFluidBar.getWidth() + 7, 0, 0), Size.of(fluidBar));

		secondOutputFluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(2));
		
		mainPanel.createChild(WHorizontalArrow::new, Position.of(fluidBar, fluidBar.getWidth() + 9, fluidBar.getHeight() / 2 - 8, 0), Size.of(22, 16))
				.setLimitSupplier(() -> electrolyzer.limit)
				.setProgressSupplier(() -> electrolyzer.current);
	}
}
