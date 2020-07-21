package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyItemHandledScreen;
import com.github.chainmailstudios.astromine.common.block.entity.AlloySmelterBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.AlloySmelterScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyItemScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.WHorizontalArrow;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class AlloySmelterHandledScreen extends DefaultedEnergyItemHandledScreen<AlloySmelterScreenHandler> {
	public AlloySmelterHandledScreen(Text name, DefaultedEnergyItemScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);

		AlloySmelterBlockEntity smelter = (AlloySmelterBlockEntity) linkedScreenHandler.blockEntity;

		WSlot firstInput = mainPanel.createChild(WSlot::new, Position.of(energyBar), Size.of(18, 18)).setInventoryNumber(1).setSlotNumber(0);
		WSlot secondInput = mainPanel.createChild(WSlot::new, Position.of(energyBar), Size.of(18, 18)).setInventoryNumber(1).setSlotNumber(1);
		WSlot output = mainPanel.createChild(WSlot::new, Position.of(energyBar), Size.of(18, 18)).setInventoryNumber(1).setSlotNumber(2);

		firstInput.centerX();
		firstInput.setPosition(Position.of(firstInput.getX() - 22, firstInput.getY() + 15 - 9, firstInput.getZ()));
		secondInput.centerX();
		secondInput.setPosition(Position.of(secondInput.getX() - 22, secondInput.getY() + 15 + 18 - 9, secondInput.getZ()));
		output.centerX();
		output.setPosition(Position.of(secondInput, 57, -9, 0));

		WHorizontalArrow arrow = mainPanel.createChild(WHorizontalArrow::new, Position.of(output, -31, 0, 0), Size.of(22, 16))
				.setLimitSupplier(() -> smelter.limit)
				.setProgressSupplier(() -> smelter.progress);
	}
}
