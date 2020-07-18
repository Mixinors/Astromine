package com.github.chainmailstudios.astromine.common.screenhandler;

import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedItemScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import spinnery.widget.WSlot;

public class CreativeBufferScreenHandler extends DefaultedItemScreenHandler {
	public CreativeBufferScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);

		addInventory(1, ItemInventoryFromInventoryComponent.of(blockEntity.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)));

		getInterface().createChild(WSlot::new).setSlotNumber(0).setInventoryNumber(1);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineScreenHandlers.CREATIVE_BUFFER;
	}
}
