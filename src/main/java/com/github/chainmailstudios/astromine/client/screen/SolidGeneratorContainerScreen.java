package com.github.chainmailstudios.astromine.client.screen;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyItemContainerScreen;
import com.github.chainmailstudios.astromine.common.block.entity.SolidGeneratorBlockEntity;
import com.github.chainmailstudios.astromine.common.container.SolidGeneratorContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyItemContainer;
import com.github.chainmailstudios.astromine.common.widget.WHorizontalArrow;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class SolidGeneratorContainerScreen extends DefaultedEnergyItemContainerScreen<SolidGeneratorContainer> {
	public SolidGeneratorContainerScreen(Text name, DefaultedEnergyItemContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);

		SolidGeneratorBlockEntity generator = (SolidGeneratorBlockEntity) linkedContainer.blockEntity;

		WSlot input = mainPanel.createChild(WSlot::new, Position.of(mainPanel, 7, energyBar.getHeight() - 12, 0), Size.of(18, 18)).setInventoryNumber(1).setSlotNumber(0);

		WHorizontalArrow arrow = mainPanel.createChild(WHorizontalArrow::new, Position.of(input, 31, 0, 1), Size.of(22, 16))
				.setLimitSupplier(() -> generator.limit)
				.setProgressSupplier(() -> generator.current);

		energyBar.setPosition(Position.of(arrow, 29, -energyBar.getHeight() / 2 + 8, 5));
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
		super.render(matrices, mouseX, mouseY, tickDelta);
	}
}
