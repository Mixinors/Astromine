package com.github.chainmailstudios.astromine.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyFluidContainerScreen;
import com.github.chainmailstudios.astromine.common.block.entity.LiquidGeneratorBlockEntity;
import com.github.chainmailstudios.astromine.common.container.LiquidGeneratorContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyFluidContainer;
import com.github.chainmailstudios.astromine.common.widget.WHorizontalArrow;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class LiquidGeneratorContainerScreen extends DefaultedEnergyFluidContainerScreen<LiquidGeneratorContainer> {
	public LiquidGeneratorContainerScreen(Text name, DefaultedEnergyFluidContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);

		LiquidGeneratorBlockEntity generator = (LiquidGeneratorBlockEntity) linkedContainer.blockEntity;

		fluidBar.setPosition(Position.of(mainPanel, 7, 20, 0));

		WHorizontalArrow arrow = mainPanel.createChild(WHorizontalArrow::new, Position.of(fluidBar, fluidBar.getWidth() + 7, fluidBar.getHeight() / 2 - 8, 0), Size.of(22, 16))
				.setLimitSupplier(() -> generator.limit)
				.setProgressSupplier(() -> generator.current);

		energyBar.setPosition(Position.of(arrow, arrow.getWidth() + 7, -energyBar.getHeight() / 2 + 8, 0));
	}
}
