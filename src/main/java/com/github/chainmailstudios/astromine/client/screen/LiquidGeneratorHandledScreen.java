package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyFluidHandledScreen;
import com.github.chainmailstudios.astromine.common.block.entity.LiquidGeneratorBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.LiquidGeneratorScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyFluidScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.WHorizontalArrow;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class LiquidGeneratorHandledScreen extends DefaultedEnergyFluidHandledScreen<LiquidGeneratorScreenHandler> {
	public LiquidGeneratorHandledScreen(Text name, DefaultedEnergyFluidScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);

		LiquidGeneratorBlockEntity generator = (LiquidGeneratorBlockEntity) linkedScreenHandler.blockEntity;

		fluidBar.setPosition(Position.of(mainPanel, 7, 20, 0));

		WHorizontalArrow arrow = mainPanel.createChild(WHorizontalArrow::new, Position.of(fluidBar, fluidBar.getWidth() + 7, fluidBar.getHeight() / 2 - 8, 0), Size.of(22, 16))
				.setLimitSupplier(() -> generator.limit)
				.setProgressSupplier(() -> (int) generator.current);

		energyBar.setPosition(Position.of(arrow, arrow.getWidth() + 7, -energyBar.getHeight() / 2 + 8, 0));
	}
}
